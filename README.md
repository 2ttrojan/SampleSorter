# SampleSorter

# Technical and Business Documentation

## Technical Assumptions and Simplifications:

1. There is one machine operating in the laboratory.
2. It communicates with SampleSorter Api via HTTP.
3. SampleSorter Api can run on multiple nodes, but there is no event synchronization in it, so the machine must take
   care of blocking the sending of the next request until the response from the previous one returns. Otherwise, a race
   condition may occur, which may result in breaking the rules of sample placement.
4. SampleSorter Api uses mock repositories, meaning data is persisted in RAM memory. Ultimately, data can come from a
   database or another api, just add the appropriate implementation.

## Business Assumptions:

1. SampleSorter Api can be used by multiple laboratories simultaneously. Each laboratory can use its own logic for
   assigning samples.
2. The basic assumption is physical limitations, e.g., rack limit, space limit on the rack. Other limitations arise from
   business needs.
3. Be aware that validation of adding already assigned sample is not covered.
4. The SampleSorter API only includes an endpoint for sequentially adding samples. It may happen that there are still
   available spaces on the racks, but adding another sample is not possible because it would violate rules regarding
   age, company, etc. Unfortunately, the SampleSorter API does not have the functionality to optimize racks to fit a
   sample. However, at the bottom of the document, there is a ready-made pseudo-algorithm that solves this problem.

## User Stories

1. Adding a sample should be possible when there is available space in the rack container and when the requirements for
   assigning sample to racks have been met.
2. Each container can have a predefined logic for assigning samples to racks.
3. If there is no available space in the rack container, a specific message should be returned so that it can be
   distinguished whether the inability to add another sample is due to lack of space or non-compliance with specific
   logic.
4. The logic for Container 1 should be defined according to the following rules:
    - it is illegal to place patients of the same age into the same rack,
    - it is illegal to place patients working in the same company into the same rack,
    - it is illegal to place patients who live in the same city district into the same rack,
    - it is illegal to place patients with the same vision defect into the same rack.

## Project structure and core objects

I used Maven modules to separate the adapter, app, and domain layers, in accordance with DDD principles.

### Adapter

The adapter here encompasses the entire configuration of beans and dependencies. This ensures that the domain does not
have to rely on a specific framework. The adapter is responsible for exposing the API, allowing for the reception of
messages from external sources, such as via REST API, but it could also be a Kafka Consumer.

### App

The app serves as a facade for the rack container and contains the implementation of sample assignment policies.
Main objects:

1. WeirdRackContainerAssignmentMetrics assigned by id to rackContainer
2. WeirdRackMetrics assigned by id to rack
   both contains data to maintain specific (weird) assignment logic.

### Domain

The domain contains the core logic for assigning samples on racks. It should not be dependent on any framework.

Main objects:

1. RackContainer is a container for Rack.
2. Rack is a place where samples are located.
3. SampleMetrics contains data like patient company, age etc

### Shared

The Shared contains common objects used in other modules.

### Monolith

The monolith is a boot starter for Spring Boot and may also contain general system parameters.

## How to build

you need Java 17 and maven (e.g. 3.8.1)
run command in main directory: mvn clean install

## How to run locally

you need Java 17 and maven (e.g. 3.8.1)
run command in monolith directory: mvn spring-boot:run
it will run application 8080 port

## How tu run tests

you need Java 17 and maven (e.g. 3.8.1)
run command in main directory: mvn test

## Mock

The application uses mocked repositories. After each launch, it reverts to the initial state.

Mock data:

### Rack container:

- id: 1
- 2 rack (id 1 and id 2) each has 2 spots where you can place sample,

### Sample Metric

Please see MockSampleMetrics.
You can use samples id: 100, 101, 102, 103, 104

# Sample API Documentation

Endpoint: Add a New Sample
URL: /rackcontainer/sample

Method: POST

Description: This endpoint is used to add a new sample to the system.

Request
Content-Type: application/json

```json
{
  "rackContainerId": "long",
  "sampleId": "long"
}
```

Response
Success (200 OK):

```json
{
  "isAdded": "boolean",
  "sampleLocation": {
    "rackContainerId": "long",
    "rackId": "long",
    "positionOnRack": "int"
  },
  "failReason": "string (NO_SPOTS_LEFT or NOT_MET_ASSIGNMENT_POLICY)"
}
```

If the sample is successfully added, isAdded will be true and sampleLocation will contain the location details. failReason will be null.
If the sample is not added, isAdded will be false and failReason will contain the reason for the failure. sampleLocation will be null.


In addition, [Postman collection](Sample_sorter.postman_collection.json)



## Algorithm to optimize rack:

### Motivation
Imagine we have 2 racks with 2 spots in each and 4 samples. </br> 
Ideally, it would be great if we could fit them all. Unfortunately, it's not that simple. Samples are labeled with a letter and a number. You cannot place the same letter or the same number on the same rack. RackContainer will give you first available rack. </br>
For instance, you can't have A1 and A4 or B1 on one rack. The order of inserting samples matters. Imagine that we insert samples in the given order: A1, A2, B3, B2.</br>
A1 will be added to rack 1. </br>
A2 will be added to rack 2. </br>
B3 will be added to rack 1. </br>
B2 cannot be added because rack 1 has B3 (the same letter) and it can't be added to rack 2 because there's A2 (the same number). </br> 
In this situation, the application returns the status NOT_MET_ASSIGNMENT_POLICY. 

### Suggested Solution for Review
Based on this, we can optimize the racks. 
It's enough to sort the samples, considering the current samples plus the new one, based on the number of repeated features (in this case, letters and numbers). </br>
The letter A repeats 2 times, so it scores 2 points. </br>
The letter B also repeats 2 times, so it scores 2 points. </br>
The number 1 repeats once, so it scores 1 point. </br>
The number 2 repeats twice, so it scores 2 points. </br></br>
Thus, A1 has 3 points, A2 has 4 points, B3 has 3 points, and B2 has 3 points. </br> 
If we sort the samples from the highest to the lowest, the first one is A2, then each has 3 points, so the order doesn't matter, making it A2, B3, B2, A1. </br> 
If we start assigning samples to empty racks in this order, we will find a place for all 4.</br>
A2 to rack1, B3 to rack1, B2 to rack2, A1 to rack2
