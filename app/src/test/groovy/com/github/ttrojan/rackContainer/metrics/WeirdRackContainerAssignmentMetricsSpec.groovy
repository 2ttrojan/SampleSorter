package com.github.ttrojan.rackContainer.metrics


import spock.lang.Specification
import spock.lang.Unroll

import static com.github.ttrojan.sample.SampleMetricsTestBuilder.*

class WeirdRackContainerAssignmentMetricsSpec extends Specification {

    def "should update metrics for a given rack"() {
        given: "a WeirdRackContainerAssignmentMetrics instance and a sample metric"
        def metrics = new WeirdRackContainerAssignmentMetrics(1L)

        def sampleMetrics = (
                sampleMetricsBuilder()
                        << PatientBirthDate.of(30)
                        << Company.of("GOOGLE")
                        << CityDistrict.of("D1")
                        << VisionDefect.of("-6")
        ).build()

        when: "updating metrics for a rack"
        metrics.updateMetrics(1L, sampleMetrics)

        then: "metrics for the rack should be updated"
        def rackMetrics = metrics.getMetricsForRack(1L).get()
        rackMetrics.utilizedAges.contains(30)
        rackMetrics.utilizedCompanies.contains("GOOGLE")
        rackMetrics.utilizedCityDistricts.contains("D1")
        rackMetrics.utilizedCityVisionDefects.contains("-6")
    }


    def "should return empty when no metrics for a given rack"() {
        given: "a WeirdRackContainerAssignmentMetrics instance without any metrics"
        def metrics = new WeirdRackContainerAssignmentMetrics(1L)

        when: "getting metrics for a rack"
        def result = metrics.getMetricsForRack(1L)

        then: "result should be empty"
        !result.isPresent()
    }


    @Unroll
    def "should return #expected when checking if metrics contains sample metrics"() {
        given: "a WeirdRackMetrics instance"
        def metrics = new WeirdRackContainerAssignmentMetrics.WeirdRackMetrics()
        and: "updating metrics with given sample metrics"
        def initialSampleMetrics = (
                sampleMetricsBuilder()
                        << PatientBirthDate.of(25)
                        << Company.of("CompanyA")
                        << CityDistrict.of("DistrictA")
                        << VisionDefect.of("DefectA")
        ).build()
        metrics.updateMetrics(initialSampleMetrics)

        when: "checking if metrics contains sample metrics"
        def result = metrics.contains(sampleMetricsBuilder.build())

        then: "result should be as expected"
        result == expected

        where:
        sampleMetricsBuilder                       | expected
        (sampleMetricsBuilder()
                << PatientBirthDate.of(25)
                << Company.of("CompanyA")
                << CityDistrict.of("DistrictA")
                << VisionDefect.of("DefectA"))     | true
        (sampleMetricsBuilder()
                << PatientBirthDate.of(30)
                << Company.of("CompanyA")
                << CityDistrict.of("DistrictA")
                << VisionDefect.of("DefectA"))     | true
        (sampleMetricsBuilder()
                << PatientBirthDate.of(25)
                << Company.of("CompanyOther")
                << CityDistrict.of("DistrictA")
                << VisionDefect.of("DefectA"))     | true
        (sampleMetricsBuilder()
                << PatientBirthDate.of(25)
                << Company.of("CompanyA")
                << CityDistrict.of("DistrictOther")
                << VisionDefect.of("DefectA"))     | true
        (sampleMetricsBuilder()
                << PatientBirthDate.of(25)
                << Company.of("CompanyA")
                << CityDistrict.of("DistrictA")
                << VisionDefect.of("DefectOther")) | true
        (sampleMetricsBuilder()
                << PatientBirthDate.of(30)
                << Company.of("CompanyOther")
                << CityDistrict.of("DistrictOther")
                << VisionDefect.of("DefectOther")) | false
    }
}
