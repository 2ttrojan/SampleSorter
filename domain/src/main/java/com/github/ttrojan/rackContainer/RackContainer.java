package com.github.ttrojan.rackContainer;

import com.github.ttrojan.NewRackDto;
import com.github.ttrojan.sample.SampleLocationDto;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class RackContainer {
    private final long rackContainerId;
    private final Set<Rack> racks;
    private final RackContainerAssignmentPolicyType assignmentPolicyType;

    public RackContainer(long rackContainerId,
                         RackContainerAssignmentPolicyType assignmentPolicyType) {
        this.rackContainerId = rackContainerId;
        this.assignmentPolicyType = assignmentPolicyType;
        this.racks = new HashSet<>();
    }

    public SampleLocationDto assignTo(long rackId, long sampleId) {
        Rack rackToAssign = racks.stream()
                .filter(rack -> rackId == rack.getRackId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Rack %s with id already exists", rackId)));

        return rackToAssign.assignSample(sampleId);
    }

    public Rack addNewRack(NewRackDto rackDto) {
        Rack rack = Rack.from(rackDto);
        if (racks.contains(rack)) {
            throw new IllegalArgumentException(String.format("Rack %s with id already exists", rack.getRackId()));
        }
        racks.add(rack);
        return rack;
    }

    public Set<Rack> getRackWithCapacity() {
        return this.racks.stream()
                .filter(Rack::hasCapacity)
                .collect(Collectors.toSet());
    }

}
