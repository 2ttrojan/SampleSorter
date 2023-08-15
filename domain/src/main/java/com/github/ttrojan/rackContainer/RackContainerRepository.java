package com.github.ttrojan.rackContainer;

import java.util.Optional;

public interface RackContainerRepository {

    Optional<RackContainer> getRackContainerById(long rackContainer);

    void save(RackContainer rackContainer);

}
