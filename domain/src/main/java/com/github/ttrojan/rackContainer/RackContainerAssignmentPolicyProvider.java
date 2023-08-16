package com.github.ttrojan.rackContainer;

import java.util.Optional;

public interface RackContainerAssignmentPolicyProvider {

    Optional<RackContainerAssignmentPolicy> getByType(RackContainerAssignmentPolicyType assignmentPolicyType);

}
