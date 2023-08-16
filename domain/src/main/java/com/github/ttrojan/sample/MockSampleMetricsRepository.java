package com.github.ttrojan.sample;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MockSampleMetricsRepository implements SampleMetricsRepository {
    private final Map<Long, SampleMetrics> data;

    public MockSampleMetricsRepository() {
        data = new HashMap<>();
        SampleMetrics sample100L = SampleMetrics.builder()
                .sampleId(100L)
                .patientId(1001L)
                .patientBirthDate(LocalDate.now().minus(30, ChronoUnit.YEARS))
                .cityDistrict("D1")
                .company("Google")
                .visionDefect("0")
                .build();

        SampleMetrics sample101L = SampleMetrics.builder()
                .sampleId(101L)
                .patientId(1001L)
                .patientBirthDate(LocalDate.now().minus(30, ChronoUnit.YEARS))
                .cityDistrict("D2")
                .company("Tesla")
                .visionDefect("-1")
                .build();

        SampleMetrics sample102L = SampleMetrics.builder()
                .sampleId(102L)
                .patientId(1002L)
                .patientBirthDate(LocalDate.now().minus(20, ChronoUnit.YEARS))
                .cityDistrict("D3")
                .company("Meta")
                .visionDefect("-2")
                .build();

        SampleMetrics sample103L = SampleMetrics.builder()
                .sampleId(103L)
                .patientId(1003L)
                .patientBirthDate(LocalDate.now().minus(25, ChronoUnit.YEARS))
                .cityDistrict("D9")
                .company("Google")
                .visionDefect("-3")
                .build();

        SampleMetrics sample104L = SampleMetrics.builder()
                .sampleId(104L)
                .patientId(1004L)
                .patientBirthDate(LocalDate.now().minus(40, ChronoUnit.YEARS))
                .cityDistrict("D1")
                .company("Meta")
                .visionDefect("-4")
                .build();

        data.put(sample100L.sampleId(), sample100L);
        data.put(sample101L.sampleId(), sample101L);
        data.put(sample102L.sampleId(), sample102L);
        data.put(sample103L.sampleId(), sample103L);
        data.put(sample104L.sampleId(), sample103L);
    }


    @Override
    public Optional<SampleMetrics> getSampleById(long id) {
        return Optional.ofNullable(data.get(id));
    }
}
