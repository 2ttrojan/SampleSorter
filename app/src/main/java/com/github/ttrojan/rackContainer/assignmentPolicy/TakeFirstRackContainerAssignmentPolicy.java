package com.github.ttrojan.rackContainer.assignmentPolicy;

import com.github.ttrojan.rackContainer.Rack;
import com.github.ttrojan.rackContainer.RackContainer;
import com.github.ttrojan.rackContainer.RackContainerAssignmentPolicy;
import com.github.ttrojan.rackContainer.RackContainerAssignmentPolicyType;
import com.github.ttrojan.sample.SampleLocationDto;
import com.github.ttrojan.sample.SampleMetrics;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class TakeFirstRackContainerAssignmentPolicy implements RackContainerAssignmentPolicy {

    @Override
    public RackContainerAssignmentPolicyType getType() {
        return RackContainerAssignmentPolicyType.TAKE_FIRST_RACK;
    }

    @Override
    public Optional<Long> getAssignableRackId(RackContainer rackContainer, SampleMetrics sampleMetrics) {
        Set<Rack> rackWithCapacity = rackContainer.getRackWithCapacity();
        if (rackWithCapacity.size() == 0) {
            return Optional.empty();
        }
        return rackWithCapacity.stream()
                .min(Comparator.comparingLong(Rack::getRackId))
                .map(Rack::getRackId);
    }

    @Override
    public void publishAssignment(SampleLocationDto sampleLocation, SampleMetrics sampleMetrics) {
    }

}
