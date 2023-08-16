package com.github.ttrojan.app;

import com.github.ttrojan.rackContainer.*;
import com.github.ttrojan.rackContainer.assignmentPolicy.TakeFirstRackContainerAssignmentPolicy;
import com.github.ttrojan.rackContainer.assignmentPolicy.WeirdRackContainerAssignmentPolicy;
import com.github.ttrojan.rackContainer.metrics.MockWeirdRackContainerAssignmentMetricsRepository;
import com.github.ttrojan.rackContainer.metrics.WeirdRackContainerAssignmentMetricsRepository;
import com.github.ttrojan.sample.MockSampleMetricsRepository;
import com.github.ttrojan.sample.SampleMetricsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfiguration {

    @Bean
    SampleMetricsRepository sampleRepository() {
        return new MockSampleMetricsRepository();
    }

    @Bean
    RackContainerRepository weirdRackMetricsRepository() {
        return new MockRackContainerRepository();
    }

    @Bean
    WeirdRackContainerAssignmentMetricsRepository weirdRackContainerAssignmentMetricsRepository() {
        return new MockWeirdRackContainerAssignmentMetricsRepository();
    }

    @Bean
    WeirdRackContainerAssignmentPolicy weirdRackContainerAssignmentPolicy(WeirdRackContainerAssignmentMetricsRepository repository) {
        return new WeirdRackContainerAssignmentPolicy(repository);
    }

    @Bean
    TakeFirstRackContainerAssignmentPolicy takeFirstRackContainerAssignmentPolicy() {
        return new TakeFirstRackContainerAssignmentPolicy();
    }

    @Bean
    RackContainerAssignmentPolicyProvider assignmentPolicyProvider(List<RackContainerAssignmentPolicy> assignmentPolicies) {
        return new RackContainerAssignmentPolicyProvider(assignmentPolicies);
    }

    @Bean
    RackContainerFacade newSampleHandler(SampleMetricsRepository sampleMetricsRepository,
                                         RackContainerRepository rackContainerRepository,
                                         RackContainerAssignmentPolicyProvider assignmentPolicyProvider) {
        return new RackContainerFacade(sampleMetricsRepository, rackContainerRepository, assignmentPolicyProvider);
    }
}
