package com.github.ttrojan.sample;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class SampleMetrics {

    private long sampleId;
    private long patientId;
    private LocalDate patientBirthDate;
    private String company;
    private String cityDistrict;
    private String visionDefect;

    /**
     * for ORMs
     */

    protected SampleMetrics() {
    }
}
