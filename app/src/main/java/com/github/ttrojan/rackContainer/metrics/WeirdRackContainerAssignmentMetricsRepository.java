package com.github.ttrojan.rackContainer.metrics;

import com.github.ttrojan.rackContainer.WeirdRackContainerAssignmentMetricsDto;

import java.util.Optional;

public interface WeirdRackContainerAssignmentMetricsRepository {

    Optional<WeirdRackContainerAssignmentMetrics> getByContainerRackId(long rackContainerId);

    void save(WeirdRackContainerAssignmentMetrics assignmentMetrics);

    WeirdRackContainerAssignmentMetricsDto getMetricsViewById(long rackContainerId);
}
