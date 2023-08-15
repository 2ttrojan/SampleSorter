package com.github.ttrojan.rackContainer;

import com.github.ttrojan.rackContainer.metrics.WeirdRackContainerAssignmentMetricsRepository;
import com.github.ttrojan.sample.NewSampleEvent;
import com.github.ttrojan.sample.NewSampleEventResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rackcontainer")
@RequiredArgsConstructor
@Slf4j
public class RackContainerController {

    private final RackContainerFacade rackContainerFacade;
    private final WeirdRackContainerAssignmentMetricsRepository weirdRackContainerAssignmentMetricsRepository;

    @PostMapping("/sample")
    public ResponseEntity<NewSampleEventResult> newSample(@RequestBody NewSampleEvent newSampleEvent) {
        return ResponseEntity.ok(rackContainerFacade.assignSample(newSampleEvent));
    }

    // Added just for fun
    @GetMapping("/weirdmetrics")
    public ResponseEntity<WeirdRackContainerAssignmentMetricsDto> getWeirdMetrics(@RequestParam Long rackContainerId) {
        return ResponseEntity.ok(weirdRackContainerAssignmentMetricsRepository.getMetricsViewById(rackContainerId));
    }
}
