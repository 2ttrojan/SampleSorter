package com.github.ttrojan.sample;

public record NewSampleEventResult(boolean isAdded, SampleLocationDto sampleLocation, NewSampleFailReason failReason) {

    public static NewSampleEventResult success(SampleLocationDto sampleLocation) {
        return new NewSampleEventResult(true, sampleLocation, null);
    }

    public static NewSampleEventResult fail(NewSampleFailReason failReason) {
        return new NewSampleEventResult(false, null, failReason);
    }

}
