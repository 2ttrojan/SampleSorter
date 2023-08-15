package com.github.ttrojan.rackContainer;

import com.github.ttrojan.sample.SampleLocationDto;
import com.github.ttrojan.sample.SampleMetrics;

import java.util.Optional;

public interface RackContainerAssignmentPolicy {

    RackContainerAssignmentPolicyType getType();

    Optional<Long> getAssignableRackId(RackContainer rackContainer, SampleMetrics sampleMetrics);

    void publishAssignment(SampleLocationDto sampleLocation, SampleMetrics sampleMetrics);
}
