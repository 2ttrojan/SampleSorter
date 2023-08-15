package com.github.ttrojan.rackContainer.metrics;

import com.github.ttrojan.rackContainer.WeirdRackContainerAssignmentMetricsDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.ttrojan.rackContainer.WeirdRackContainerAssignmentMetricsDto.WeirdRackMetricsDto;

public class MockWeirdRackContainerAssignmentMetricsRepository implements WeirdRackContainerAssignmentMetricsRepository {

    private final Map<Long, WeirdRackContainerAssignmentMetrics> data;

    public MockWeirdRackContainerAssignmentMetricsRepository() {
        data = new HashMap<>();
        long rackContainerId = 1L;
        data.put(rackContainerId, new WeirdRackContainerAssignmentMetrics(rackContainerId));
    }

    @Override
    public Optional<WeirdRackContainerAssignmentMetrics> getByContainerRackId(long rackContainerId) {
        return Optional.ofNullable(data.get(rackContainerId));
    }

    @Override
    public void save(WeirdRackContainerAssignmentMetrics assignmentMetrics) {
        data.put(assignmentMetrics.getRackContainerId(), assignmentMetrics);
    }

    @Override
    public WeirdRackContainerAssignmentMetricsDto getMetricsViewById(long rackContainerId) {
        WeirdRackContainerAssignmentMetrics assignmentMetrics = data.get(rackContainerId);
        if (assignmentMetrics == null) {
            return new WeirdRackContainerAssignmentMetricsDto(new HashMap<>());
        }
        Map<Long, WeirdRackMetricsDto> viewMetricsMap = assignmentMetrics.getRackMetricsMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> new WeirdRackMetricsDto(
                        entry.getValue().getUtilizedAges(),
                        entry.getValue().getUtilizedCompanies(),
                        entry.getValue().getUtilizedCityDistricts(),
                        entry.getValue().getUtilizedCityVisionDefects())));
        return new WeirdRackContainerAssignmentMetricsDto(viewMetricsMap);
    }
}
