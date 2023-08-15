package com.github.ttrojan.rackContainer;

import com.github.ttrojan.sample.*;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class RackContainerFacade {

    private final SampleMetricsRepository sampleMetricsRepository;
    private final RackContainerRepository rackContainerRepository;
    private final RackContainerAssignmentPolicyProvider assignmentPolicyProvider;

    public NewSampleEventResult assignSample(NewSampleEvent newSampleEvent) {
        //TODO:
        // Implement a verification to check if the rackContainerId is valid.
        // For instance, in the header, each sample machine can transmit a unique token.
        // We can then retrieve this token and validate if the provided rackContainerId is associated with that specific machine token
        // Of course, this implies that we first need to generate a token and associate it with the respective rackContainerIds

        RackContainer rackContainer = getRackContainer(newSampleEvent.rackContainerId());
        SampleMetrics sampleMetrics = getSampleMetrics(newSampleEvent.sampleId());
        RackContainerAssignmentPolicy assignmentPolicy = getRackContainerAssignmentPolicy(rackContainer.getAssignmentPolicyType());

        Optional<Long> maybeAssignableRackId = assignmentPolicy.getAssignableRackId(rackContainer, sampleMetrics);
        if (maybeAssignableRackId.isPresent()) {
            long rackId = maybeAssignableRackId.get();
            long sampleId = sampleMetrics.getSampleId();
            try {
                SampleLocationDto sampleLocation = rackContainer.assignTo(rackId, sampleId);
                rackContainerRepository.save(rackContainer);
                assignmentPolicy.publishAssignment(sampleLocation, sampleMetrics);
                return NewSampleEventResult.success(sampleLocation);
            } catch (Throwable th) {
                rollbackAssignment(rackId, sampleId);
                throw th;
            }
        }
        return NewSampleEventResult.fail();
    }

    private RackContainerAssignmentPolicy getRackContainerAssignmentPolicy(RackContainerAssignmentPolicyType assignmentPolicyType) {
        return assignmentPolicyProvider
                .getByType(assignmentPolicyType)
                .orElseThrow(() -> new IllegalArgumentException("Cannot provider RackContainerAssignmentPolicy for: " + assignmentPolicyType));
    }

    private RackContainer getRackContainer(long rackContainerId) {
        return rackContainerRepository.getRackContainerById(rackContainerId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot fetch rack container for: " + rackContainerId));
    }

    private SampleMetrics getSampleMetrics(long sampleId) {
        return sampleMetricsRepository.getSampleById(sampleId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot fetch SampleMetrics by sampleId: " + sampleId));
    }

    private void rollbackAssignment(Long rackId, long sampleId) {
        //TODO:
        // The database can be rolled back using the @Transactional annotation on a method.
        // However, other services must be explicitly notified.
    }
}