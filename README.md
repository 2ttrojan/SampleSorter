# SampleSorter

# Technical and Business Documentation

## Technical Assumptions and Simplifications:
1. There is one machine operating in the laboratory.
2. It communicates with SampleSorter Api via HTTP.
3. SampleSorter Api can run on multiple nodes, but there is no event synchronization in it, so the machine must take care of blocking the sending of the next request until the response from the previous one returns. Otherwise, a race condition may occur, which may result in breaking the rules of sample placement.
4. SampleSorter Api uses mock repositories, meaning data is persisted in RAM memory. Ultimately, data can come from a database or another api, just add the appropriate implementation.

## Business Assumptions:
1. SampleSorter Api can be used by multiple laboratories simultaneously. Each laboratory can use its own logic for assigning samples.
2. The basic assumption is physical limitations, e.g., rack limit, space limit on the rack. Other limitations arise from business needs.