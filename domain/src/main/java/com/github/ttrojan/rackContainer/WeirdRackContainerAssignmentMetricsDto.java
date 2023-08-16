package com.github.ttrojan.rackContainer;

import java.util.Map;
import java.util.Set;

public record WeirdRackContainerAssignmentMetricsDto(Map<Long, WeirdRackMetricsDto> rackMetricsMap) {

    public record WeirdRackMetricsDto(Set<Integer> utilizedAges,
                                      Set<String> utilizedCompanies,
                                      Set<String> utilizedCityDistricts,
                                      Set<String> utilizedCityVisionDefects) {
    }
}
