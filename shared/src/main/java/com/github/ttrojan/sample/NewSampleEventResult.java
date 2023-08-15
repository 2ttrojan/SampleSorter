package com.github.ttrojan.sample;

public record NewSampleEventResult(boolean isAdded, SampleLocationDto sampleLocation) {

    public static NewSampleEventResult success(SampleLocationDto sampleLocation) {
        return new NewSampleEventResult(true, sampleLocation);
    }

    public static NewSampleEventResult fail() {
        return new NewSampleEventResult(false, null);
    }

}
