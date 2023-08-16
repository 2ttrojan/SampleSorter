package com.github.ttrojan.rackContainer


import spock.lang.Specification

import static com.github.ttrojan.rackContainer.RackTestBuilder.Capacity
import static com.github.ttrojan.rackContainer.RackTestBuilder.rackBuilder

class RackSpec extends Specification {

    def "should assign sample when capacity is available"() {
        given: "a rack with available capacity"
        Rack rack = (
                rackBuilder() << Capacity.of(1)
        ).build()

        when: "assigning a sample"
        def result = rack.assignSample(100L)

        then: "sample is assigned and correct DTO is returned"
        noExceptionThrown()
        result.rackContainerId() == 1
        result.rackId() == 1
        result.positionOnRack() == 1
    }

    def "should throw exception when no capacity left"() {
        given: "a rack with no available capacity"
        Rack rack = (
                rackBuilder() << Capacity.of(2, 1)
        ).build()
        rack.assignSample(100L)

        when: "assigning another sample"
        rack.assignSample(101L)

        then: "an exception is thrown with correct message"
        def th = thrown(IllegalStateException)
        th.message == Rack.CANNOT_ASSIGN_MORE_SAMPLES_TO_RACK_EX_MSG + rack.rackId
    }
}
