package com.github.ttrojan.rackContainer;

import lombok.Value;

import java.util.Map;
import java.util.Set;

@Value
public class WeirdRackContainerAssignmentMetricsDto {

    Map<Long, WeirdRackMetricsDto> rackMetricsMap;

    @Value
    public static class WeirdRackMetricsDto {
        Set<Integer> utilizedAges;
        Set<String> utilizedCompanies;
        Set<String> utilizedCityDistricts;
        Set<String> utilizedCityVisionDefects;
    }
}
