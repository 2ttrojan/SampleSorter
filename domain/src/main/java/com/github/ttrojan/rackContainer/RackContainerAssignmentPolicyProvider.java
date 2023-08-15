package com.github.ttrojan.rackContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RackContainerAssignmentPolicyProvider {

    private final Map<RackContainerAssignmentPolicyType, RackContainerAssignmentPolicy> policyMap;

    public RackContainerAssignmentPolicyProvider(List<RackContainerAssignmentPolicy> assignmentPolicies) {
        this.policyMap = new HashMap<>();
        assignmentPolicies.forEach(assignmentPolicy -> {
            this.policyMap.put(assignmentPolicy.getType(), assignmentPolicy);
        });
    }

    public Optional<RackContainerAssignmentPolicy> getByType(RackContainerAssignmentPolicyType assignmentPolicyType) {
        return Optional.ofNullable(this.policyMap.get(assignmentPolicyType));
    }

}
