package com.github.ttrojan.rackContainer;

import com.github.ttrojan.NewRackDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.github.ttrojan.rackContainer.RackContainerAssignmentPolicyType.WEIRD_LOGIC;

public class MockRackContainerRepository implements RackContainerRepository {

    private final Map<Long, RackContainer> data;

    public MockRackContainerRepository() {
        data = new HashMap<>();
        long rackContainerId = 1L;
        RackContainer rackContainer = new RackContainer(rackContainerId, WEIRD_LOGIC);
        rackContainer.addNewRack(new NewRackDto(1L, rackContainerId, 2));
        rackContainer.addNewRack(new NewRackDto(2L, rackContainerId, 2));
        data.put(rackContainerId, rackContainer);
    }

    @Override
    public Optional<RackContainer> getRackContainerById(long rackContainer) {
        return Optional.ofNullable(data.get(rackContainer));
    }

    @Override
    public void save(RackContainer rackContainer) {
        data.put(rackContainer.getRackContainerId(), rackContainer);
    }
}
