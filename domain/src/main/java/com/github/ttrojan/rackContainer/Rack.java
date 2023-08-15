package com.github.ttrojan.rackContainer;

import com.github.ttrojan.NewRackDto;
import com.github.ttrojan.sample.SampleLocationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
public class Rack {

    private long rackId;
    private long rackContainerId;
    private int initialCapacity;
    private int capacity;

    private Set<SampleAssignment> sampleAssignments;

    protected Rack() {
    }

    public static Rack from(NewRackDto newRackDto) {
        return new Rack(
                newRackDto.rackId(),
                newRackDto.rackContainerId(),
                newRackDto.capacity(),
                newRackDto.capacity(),
                new HashSet<>()
        );
    }

    public SampleLocationDto assignSample(long sampleId) {
        if (capacity == 0) {
            throw new IllegalStateException("Cannot assign more samples to rack" + rackId);
        }
        capacity--;
        int nextPositionOnRack = initialCapacity - capacity;
        sampleAssignments.add(new SampleAssignment(sampleId, rackContainerId, rackId, nextPositionOnRack));
        return new SampleLocationDto(rackContainerId, rackId, nextPositionOnRack);
    }

    public boolean hasCapacity() {
        return capacity > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rack rack = (Rack) o;

        if (rackId != rack.rackId) return false;
        return rackContainerId == rack.rackContainerId;
    }

    @Override
    public int hashCode() {
        int result = (int) (rackId ^ (rackId >>> 32));
        result = 31 * result + (int) (rackContainerId ^ (rackContainerId >>> 32));
        return result;
    }
}
