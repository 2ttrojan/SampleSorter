package com.github.ttrojan.rackContainer

import com.github.ttrojan.NewRackDto
import com.github.ttrojan.sample.NewSampleEvent
import com.github.ttrojan.sample.SampleMetrics
import com.github.ttrojan.sample.SampleMetricsRepository
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.ttrojan.rackContainer.RackContainerTestBuilder.rackContainerBuilder
import static com.github.ttrojan.sample.NewSampleFailReason.NOT_MET_ASSIGNMENT_POLICY
import static com.github.ttrojan.sample.NewSampleFailReason.NO_SPOTS_LEFT

class RackContainerFacadeTest extends Specification {

    public static final long RACK_CONTAINER_ID = 1L
    public static final long RACK_ID = 10L
    public static final long SAMPLE_ID = 100L
    public static final String EXCEPTION_MESSAGE = "Test exception"

    @Unroll
    def "should assign sample successfully when assignable rack id is present"() {
        given: "new sample event"
        def newSampleEvent = new NewSampleEvent(RACK_CONTAINER_ID, SAMPLE_ID)

        and: "rack container with one rack"
        def rackContainer = Mock(RackContainer)
        RackContainerRepository rackContainerRepository = Mock()
        rackContainerRepository.getRackContainerById(RACK_CONTAINER_ID) >> Optional.of(rackContainer)

        and: "sample metrics"
        def sampleMetrics = GroovyMock(SampleMetrics)
        SampleMetricsRepository sampleMetricsRepository = Mock()
        sampleMetricsRepository.getSampleById(SAMPLE_ID) >> Optional.of(sampleMetrics)

        and: "mock assignment policy"
        def assignmentPolicy = Mock(RackContainerAssignmentPolicy)
        RackContainerAssignmentPolicyProvider assignmentPolicyProvider = Mock()
        assignmentPolicyProvider.getByType(rackContainer.getAssignmentPolicyType()) >> Optional.of(assignmentPolicy)
        assignmentPolicy.getAssignableRackId(rackContainer, sampleMetrics) >> Optional.of(RACK_ID)

        and: "create facade"
        RackContainerFacade rackContainerFacade = new RackContainerFacade(
                sampleMetricsRepository,
                rackContainerRepository,
                assignmentPolicyProvider
        )

        when:
        def sampleEventResult = rackContainerFacade.assignSample(newSampleEvent)

        then:
        1 * rackContainer.assignTo(_, _)
        1 * rackContainerRepository.save(rackContainer)
        1 * assignmentPolicy.publishAssignment(_, _)
        sampleEventResult.added
    }

    @Unroll
    def "should return NO_SPOTS_LEFT when rack container is full"() {
        given: "new sample event"
        def newSampleEvent = new NewSampleEvent(RACK_CONTAINER_ID, SAMPLE_ID)

        and: "rack container with one rack and already used available spots"
        def rackContainer = rackContainerBuilder().build()
        rackContainer.addNewRack(new NewRackDto(RACK_ID, RACK_CONTAINER_ID, 1))
        rackContainer.assignTo(RACK_ID, 123L)

        RackContainerRepository rackContainerRepository = Mock()
        rackContainerRepository.getRackContainerById(RACK_CONTAINER_ID) >> Optional.of(rackContainer)

        and: "sample metrics"
        def sampleMetrics = GroovyMock(SampleMetrics)
        SampleMetricsRepository sampleMetricsRepository = Mock()
        sampleMetricsRepository.getSampleById(SAMPLE_ID) >> Optional.of(sampleMetrics)

        and: "mock assignment policy"
        def assignmentPolicy = Mock(RackContainerAssignmentPolicy)
        RackContainerAssignmentPolicyProvider assignmentPolicyProvider = Mock()
        assignmentPolicyProvider.getByType(rackContainer.getAssignmentPolicyType()) >> Optional.of(assignmentPolicy)
        assignmentPolicy.getAssignableRackId(rackContainer, sampleMetrics) >> Optional.of(RACK_ID)

        and: "create facade"
        RackContainerFacade rackContainerFacade = new RackContainerFacade(
                sampleMetricsRepository,
                rackContainerRepository,
                assignmentPolicyProvider
        )

        when:
        def sampleEventResult = rackContainerFacade.assignSample(newSampleEvent)

        then:
        !sampleEventResult.added
        sampleEventResult.failReason() == NO_SPOTS_LEFT
    }


    @Unroll
    def "should return NOT_MET_ASSIGNMENT_POLICY when is not full but it cannot be placed on any rack"() {
        given: "new sample event"
        def newSampleEvent = new NewSampleEvent(RACK_CONTAINER_ID, SAMPLE_ID)

        and: "rack container with one rack"
        def rackContainer = Mock(RackContainer)
        RackContainerRepository rackContainerRepository = Mock()
        rackContainerRepository.getRackContainerById(RACK_CONTAINER_ID) >> Optional.of(rackContainer)

        and: "sample metrics"
        def sampleMetrics = GroovyMock(SampleMetrics)
        SampleMetricsRepository sampleMetricsRepository = Mock()
        sampleMetricsRepository.getSampleById(SAMPLE_ID) >> Optional.of(sampleMetrics)

        and: "mock assignment policy that for getAssignableRackId will return empty"
        def assignmentPolicy = Mock(RackContainerAssignmentPolicy)
        RackContainerAssignmentPolicyProvider assignmentPolicyProvider = Mock()
        assignmentPolicyProvider.getByType(rackContainer.getAssignmentPolicyType()) >> Optional.of(assignmentPolicy)
        assignmentPolicy.getAssignableRackId(rackContainer, sampleMetrics) >> Optional.empty()

        and: "create facade"
        RackContainerFacade rackContainerFacade = new RackContainerFacade(
                sampleMetricsRepository,
                rackContainerRepository,
                assignmentPolicyProvider
        )

        when:
        def sampleEventResult = rackContainerFacade.assignSample(newSampleEvent)

        then:
        !sampleEventResult.added
        sampleEventResult.failReason() == NOT_MET_ASSIGNMENT_POLICY
    }


    @Unroll
    def "should invoke rollbackAssignment() when encountering an error on assignment"() {
        given: "new sample event"
        def newSampleEvent = new NewSampleEvent(RACK_CONTAINER_ID, SAMPLE_ID)


        and: "rack container with one rack"
        def rackContainer = Mock(RackContainer)
        RackContainerRepository rackContainerRepository = Mock()
        rackContainerRepository.getRackContainerById(RACK_CONTAINER_ID) >> Optional.of(rackContainer)
        and: "sample metrics"
        def sampleMetrics = GroovyMock(SampleMetrics)
        SampleMetricsRepository sampleMetricsRepository = Mock()
        sampleMetricsRepository.getSampleById(SAMPLE_ID) >> Optional.of(sampleMetrics)

        and: "mock assignment policy"
        def assignmentPolicy = Mock(RackContainerAssignmentPolicy)
        RackContainerAssignmentPolicyProvider assignmentPolicyProvider = Mock()
        assignmentPolicyProvider.getByType(rackContainer.getAssignmentPolicyType()) >> Optional.of(assignmentPolicy)
        assignmentPolicy.getAssignableRackId(rackContainer, sampleMetrics) >> Optional.of(RACK_ID)
        assignmentPolicy.publishAssignment(_, _) >> { throw new RuntimeException(EXCEPTION_MESSAGE) }
        and: "create facade"
        RackContainerFacade rackContainerFacade = new RackContainerFacade(
                sampleMetricsRepository,
                rackContainerRepository,
                assignmentPolicyProvider
        )

        when:
        rackContainerFacade.assignSample(newSampleEvent)

        then:
        1 * assignmentPolicy.rollbackAssignment(_, _)
        def runtimeException = thrown(RuntimeException)
        runtimeException.message == EXCEPTION_MESSAGE
    }

}
