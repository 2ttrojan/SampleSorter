package com.github.ttrojan.sample;

import java.util.Optional;

public interface SampleMetricsRepository {

    Optional<SampleMetrics> getSampleById(long id);

}
