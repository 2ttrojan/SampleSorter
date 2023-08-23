package com.github.ttrojan.rackContainer;

import com.github.ttrojan.sample.*;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.github.ttrojan.sample.NewSampleFailReason.NOT_MET_ASSIGNMENT_POLICY;
import static com.github.ttrojan.sample.NewSampleFailReason.NO_SPOTS_LEFT;

@RequiredArgsConstructor
public class RackContainerFacade {

    private final SampleMetricsRepository sampleMetricsRepository;
    private final RackContainerRepository rackContainerRepository;
    private final RackContainerAssignmentPolicyProvider assignmentPolicyProvider;

    //TODO:
    // Implement a verification to check if the rackContainerId is valid.
    // For insta nce, in the header, each sample machine can transmit a unique token.
    // We can then retrieve this token and validate if the provided rackContainerId is associated with that specific machine token
    // Of course, this implies that we first need to generate a token and associate it with the respective rackContainerIds
    public NewSampleEventResult assignSample(NewSampleEvent newSampleEvent) {
        RackContainer rackContainer = getRackContainer(newSampleEvent.rackContainerId());
        if (rackContainer.isRackContainerFull()) {
            return NewSampleEventResult.fail(NO_SPOTS_LEFT);
        }
        SampleMetrics sampleMetrics = getSampleMetrics(newSampleEvent.sampleId());
        return getAssignableRackId(rackContainer, sampleMetrics)
                .map(rackId -> processAssignment(rackId, sampleMetrics, rackContainer))
                .orElse(NewSampleEventResult.fail(NOT_MET_ASSIGNMENT_POLICY));
    }

    private Optional<Long> getAssignableRackId(RackContainer rackContainer, SampleMetrics sampleMetrics) {
        RackContainerAssignmentPolicyType assignmentPolicyType = rackContainer.getAssignmentPolicyType();
        RackContainerAssignmentPolicy assignmentPolicy = getRackContainerAssignmentPolicy(assignmentPolicyType);
        return assignmentPolicy.getAssignableRackId(rackContainer, sampleMetrics);
    }

    private NewSampleEventResult processAssignment(long rackId, SampleMetrics sampleMetrics, RackContainer rackContainer) {
        SampleLocationDto sampleLocation = rackContainer.assignTo(rackId, sampleMetrics.sampleId());
        RackContainerAssignmentPolicyType assignmentPolicyType = rackContainer.getAssignmentPolicyType();
        RackContainerAssignmentPolicy assignmentPolicy = getRackContainerAssignmentPolicy(assignmentPolicyType);
        try {
            rackContainerRepository.save(rackContainer);
            assignmentPolicy.publishAssignment(sampleLocation, sampleMetrics);
            return NewSampleEventResult.success(sampleLocation);
        } catch (Throwable th) {
            assignmentPolicy.rollbackAssignment(sampleLocation, sampleMetrics);
            throw th;
        }
    }

    private RackContainer getRackContainer(long rackContainerId) {
        return rackContainerRepository.getRackContainerById(rackContainerId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot fetch rack container for: " + rackContainerId));
    }

    private SampleMetrics getSampleMetrics(long sampleId) {
        return sampleMetricsRepository.getSampleById(sampleId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot fetch SampleMetrics by sampleId: " + sampleId));
    }

    private RackContainerAssignmentPolicy getRackContainerAssignmentPolicy(RackContainerAssignmentPolicyType assignmentPolicyType) {
        return assignmentPolicyProvider
                .getByType(assignmentPolicyType)
                .orElseThrow(() -> new IllegalArgumentException("Cannot provider RackContainerAssignmentPolicy for: " + assignmentPolicyType));
    }
}
