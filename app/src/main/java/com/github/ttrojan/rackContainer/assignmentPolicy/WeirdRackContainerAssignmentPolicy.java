package com.github.ttrojan.rackContainer.assignmentPolicy;

import com.github.ttrojan.rackContainer.Rack;
import com.github.ttrojan.rackContainer.RackContainer;
import com.github.ttrojan.rackContainer.RackContainerAssignmentPolicy;
import com.github.ttrojan.rackContainer.RackContainerAssignmentPolicyType;
import com.github.ttrojan.rackContainer.metrics.WeirdRackContainerAssignmentMetrics;
import com.github.ttrojan.rackContainer.metrics.WeirdRackContainerAssignmentMetricsRepository;
import com.github.ttrojan.sample.SampleLocationDto;
import com.github.ttrojan.sample.SampleMetrics;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class WeirdRackContainerAssignmentPolicy implements RackContainerAssignmentPolicy {

    private final WeirdRackContainerAssignmentMetricsRepository assignmentMetricsRepository;

    @Override
    public RackContainerAssignmentPolicyType getType() {
        return RackContainerAssignmentPolicyType.WEIRD_LOGIC;
    }

    @Override
    public Optional<Long> getAssignableRackId(RackContainer rackContainer, SampleMetrics sampleMetrics) {
        Set<Rack> rackWithCapacity = rackContainer.getRackWithCapacity();
        if (rackWithCapacity.size() == 0) {
            return Optional.empty();
        }

        Optional<WeirdRackContainerAssignmentMetrics> maybeMetrics = assignmentMetricsRepository.getByContainerRackId(
                rackContainer.getRackContainerId()
        );

        if (maybeMetrics.isEmpty()) {
            return Optional.of(rackWithCapacity.iterator().next().getRackId());
        }

        WeirdRackContainerAssignmentMetrics assignmentMetrics = maybeMetrics.get();

        WeirdLogicAssignmentPredicate weirdLogicAssignmentPredicate = new WeirdLogicAssignmentPredicate(
                sampleMetrics,
                assignmentMetrics
        );
        return rackWithCapacity.stream()
                .filter(weirdLogicAssignmentPredicate)
                .findFirst()
                .map(Rack::getRackId);
    }


    @Override
    public void publishAssignment(SampleLocationDto sampleLocation, SampleMetrics sampleMetrics) {
        Optional<WeirdRackContainerAssignmentMetrics> maybeAssignmentMetrics
                = assignmentMetricsRepository.getByContainerRackId(sampleLocation.rackContainerId());
        WeirdRackContainerAssignmentMetrics assignmentMetrics;
        if (maybeAssignmentMetrics.isEmpty()) {
            assignmentMetrics = new WeirdRackContainerAssignmentMetrics(sampleLocation.rackContainerId());
        } else {
            assignmentMetrics = maybeAssignmentMetrics.get();
        }

        assignmentMetrics.updateMetrics(sampleLocation.rackId(), sampleMetrics);
    }


    public static class WeirdLogicAssignmentPredicate implements Predicate<Rack> {

        private final SampleMetrics sampleMetrics;
        private final WeirdRackContainerAssignmentMetrics assignmentMetrics;

        public WeirdLogicAssignmentPredicate(SampleMetrics sampleMetrics,
                                             WeirdRackContainerAssignmentMetrics assignmentMetrics) {
            this.sampleMetrics = sampleMetrics;
            this.assignmentMetrics = assignmentMetrics;
        }

        @Override
        public boolean test(Rack rack) {
            Optional<WeirdRackContainerAssignmentMetrics.WeirdRackMetrics> maybeMetricsForRack
                    = assignmentMetrics.getMetricsForRack(rack.getRackId());
            if (maybeMetricsForRack.isEmpty()) {
                return true;
            }
            WeirdRackContainerAssignmentMetrics.WeirdRackMetrics weirdRackMetrics = maybeMetricsForRack.get();
            return !weirdRackMetrics.contains(sampleMetrics);
        }
    }

}
