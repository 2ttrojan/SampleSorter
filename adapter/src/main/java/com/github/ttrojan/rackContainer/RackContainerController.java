package com.github.ttrojan.rackContainer;

import com.github.ttrojan.sample.NewSampleEvent;
import com.github.ttrojan.sample.NewSampleEventResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rackcontainer")
@RequiredArgsConstructor
public class RackContainerController {
    private final RackContainerFacade rackContainerFacade;

    @PostMapping("/sample")
    public ResponseEntity<NewSampleEventResult> newSample(@RequestBody NewSampleEvent newSampleEvent) {
        return ResponseEntity.ok(rackContainerFacade.assignSample(newSampleEvent));
    }
}
