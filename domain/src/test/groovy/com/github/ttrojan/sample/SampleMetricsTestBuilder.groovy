package com.github.ttrojan.sample


import java.time.LocalDate

import static java.time.temporal.ChronoUnit.YEARS

class SampleMetricsTestBuilder {

    static long nextPatientId = 1L;
    static long nextSampleId = 1L;

    static sampleMetricsBuilder() {
        return new SampleMetricsTestBuilder();
    }

    SampleId sampleId = SampleId.of(takeNextSampleId())
    PatientId patientId = PatientId.of(takeNextPatientId())
    PatientBirthDate patientBirthDate = PatientBirthDate.of(20)
    Company company = Company.of("GOOGLE")
    CityDistrict cityDistrict = CityDistrict.of("D1")
    VisionDefect visionDefect = VisionDefect.of("-3")

    SampleMetricsTestBuilder leftShift(SampleId sampleId) {
        this.sampleId = sampleId
        return this
    }


    SampleMetricsTestBuilder leftShift(PatientId patientId) {
        this.patientId = patientId
        return this
    }


    SampleMetricsTestBuilder leftShift(PatientBirthDate patientBirthDate) {
        this.patientBirthDate = patientBirthDate
        return this
    }

    SampleMetricsTestBuilder leftShift(Company company) {
        this.company = company
        return this
    }

    SampleMetricsTestBuilder leftShift(CityDistrict cityDistrict) {
        this.cityDistrict = cityDistrict
        return this
    }

    SampleMetricsTestBuilder leftShift(VisionDefect visionDefect) {
        this.visionDefect = visionDefect;
        return this
    }

    SampleMetrics build() {
        return new SampleMetrics(sampleId.id,
                patientId.id,
                patientBirthDate.birthDate,
                company.value,
                cityDistrict.value,
                visionDefect.value
        )
    }

    synchronized takeNextSampleId() {
        return nextSampleId++
    }

    synchronized takeNextPatientId() {
        return nextPatientId++
    }

    static class Capacity {
        int initialCapacity;
        int leftCapacity;

        Capacity(int initialCapacity, int leftCapacity) {
            this.initialCapacity = initialCapacity
            this.leftCapacity = leftCapacity
        }

        static Capacity of(int initialCapacity) {
            return new Capacity(initialCapacity, initialCapacity);
        }

        static Capacity of(int initialCapacity, int leftCapacity) {
            return new Capacity(initialCapacity, leftCapacity);
        }

    }


    static class SampleId {
        long id;

        SampleId(long id) {
            this.id = id
        }

        static SampleId of(long id) {
            return new SampleId(id);
        }
    }

    static class PatientId {
        long id;

        PatientId(long id) {
            this.id = id
        }

        static PatientId of(long id) {
            return new PatientId(id);
        }
    }

    static class Company {
        String value;

        Company(String value) {
            this.value = value
        }

        static Company of(String value) {
            return new Company(value);
        }
    }

    static class CityDistrict {
        String value;

        CityDistrict(String value) {
            this.value = value
        }

        static CityDistrict of(String value) {
            return new CityDistrict(value);
        }
    }

    static class VisionDefect {
        String value;

        VisionDefect(String value) {
            this.value = value
        }

        static VisionDefect of(String value) {
            return new VisionDefect(value);
        }
    }

    static class PatientBirthDate {
        LocalDate birthDate;

        PatientBirthDate(int age) {
            this.birthDate = LocalDate.now().minus(age, YEARS)
        }

        static PatientBirthDate of(int age) {
            return new PatientBirthDate(age);
        }
    }
}
