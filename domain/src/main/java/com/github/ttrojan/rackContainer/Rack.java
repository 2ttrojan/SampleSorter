package com.github.ttrojan.rackContainer;

import com.github.ttrojan.NewRackDto;
import com.github.ttrojan.sample.SampleLocationDto;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Rack {

    static final String CANNOT_ASSIGN_MORE_SAMPLES_TO_RACK_EX_MSG = "Cannot assign more samples to rack";
    private final long rackId;
    private final long rackContainerId;
    private final int initialCapacity;
    private final Set<SampleAssignment> sampleAssignments;
    private int capacity;

    public Rack(long rackId, long rackContainerId, int initialCapacity) {
        this.rackId = rackId;
        this.rackContainerId = rackContainerId;
        this.initialCapacity = initialCapacity;
        this.sampleAssignments = new HashSet<>();
        this.capacity = initialCapacity;
    }

    Rack(long rackId, long rackContainerId, int initialCapacity, int capacity) {
        this.rackId = rackId;
        this.rackContainerId = rackContainerId;
        this.initialCapacity = initialCapacity;
        this.sampleAssignments = new HashSet<>();
        this.capacity = capacity;
    }

    public static Rack from(NewRackDto newRackDto) {
        return new Rack(
                newRackDto.rackId(),
                newRackDto.rackContainerId(),
                newRackDto.initialCapacity()
        );
    }

    public SampleLocationDto assignSample(long sampleId) {
        if (capacity == 0) {
            throw new IllegalStateException(CANNOT_ASSIGN_MORE_SAMPLES_TO_RACK_EX_MSG + rackId);
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
