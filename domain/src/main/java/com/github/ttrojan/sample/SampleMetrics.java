package com.github.ttrojan.sample;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SampleMetrics(long sampleId,
                            long patientId,
                            LocalDate patientBirthDate,
                            String company,
                            String cityDistrict,
                            String visionDefect) {
}
