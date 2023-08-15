package com.github.ttrojan.rackContainer.metrics;

import com.github.ttrojan.sample.SampleMetrics;
import com.github.ttrojan.vo.Age;
import lombok.Value;

import java.util.*;

public class WeirdRackContainerAssignmentMetrics {

    private final Long rackContainerId;
    private final Map<Long, WeirdRackMetrics> rackMetricsMap;

    public WeirdRackContainerAssignmentMetrics(long rackContainerId) {
        this.rackContainerId = rackContainerId;
        this.rackMetricsMap = new HashMap<>();
    }

    public void updateMetrics(long rackId, SampleMetrics sampleMetrics) {
        WeirdRackMetrics weirdRackMetrics = rackMetricsMap.get(rackId);
        if (weirdRackMetrics == null) {
            weirdRackMetrics = new WeirdRackMetrics();
        }
        weirdRackMetrics.updateMetrics(sampleMetrics);
        rackMetricsMap.put(rackId, weirdRackMetrics);
    }

    public Optional<WeirdRackMetrics> getMetricsForRack(Long rackId) {
        return Optional.ofNullable(rackMetricsMap.get(rackId));
    }

    public Map<Long, WeirdRackMetrics> getRackMetricsMap() {
        return rackMetricsMap;
    }

    public Long getRackContainerId() {
        return rackContainerId;
    }

    @Value
    public static class WeirdRackMetrics {
        Set<Integer> utilizedAges;
        Set<String> utilizedCompanies;
        Set<String> utilizedCityDistricts;
        Set<String> utilizedCityVisionDefects;

        public WeirdRackMetrics() {
            this.utilizedAges = new HashSet<>();
            this.utilizedCompanies = new HashSet<>();
            this.utilizedCityDistricts = new HashSet<>();
            this.utilizedCityVisionDefects = new HashSet<>();
        }

        public void updateMetrics(SampleMetrics sampleMetrics) {
            utilizedAges.add(Age.of(sampleMetrics.getPatientBirthDate()).getYears());
            utilizedCompanies.add(sampleMetrics.getCompany());
            utilizedCityDistricts.add(sampleMetrics.getCityDistrict());
            utilizedCityVisionDefects.add(sampleMetrics.getVisionDefect());
        }

        public boolean contains(SampleMetrics sampleMetrics) {
            if (utilizedAges.contains(Age.of(sampleMetrics.getPatientBirthDate()).getYears())) {
                return true;
            }
            if (utilizedCompanies.contains(sampleMetrics.getCompany())) {
                return true;
            }
            if (utilizedCityDistricts.contains(sampleMetrics.getCityDistrict())) {
                return true;
            }
            return utilizedCityVisionDefects.contains(sampleMetrics.getVisionDefect());
        }
    }
}
