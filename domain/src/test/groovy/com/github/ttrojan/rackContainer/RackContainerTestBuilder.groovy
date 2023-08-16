package com.github.ttrojan.rackContainer

class RackContainerTestBuilder {

    static rackContainerBuilder() {
        return new RackContainerTestBuilder();
    }

    ContainerRackId containerRackId = ContainerRackId.of(1L)
    RackContainerAssignmentPolicyType assignmentPolicyType = RackContainerAssignmentPolicyType.WEIRD_LOGIC

    RackContainerTestBuilder leftShift(RackContainerAssignmentPolicyType assignmentPolicyType) {
        this.assignmentPolicyType = assignmentPolicyType
        return this
    }

    RackContainerTestBuilder leftShift(ContainerRackId containerRackId) {
        this.containerRackId = containerRackId;
        return this
    }

    RackContainer build() {
        return new RackContainer(containerRackId.id, assignmentPolicyType)
    }

    static class ContainerRackId {
        long id;

        ContainerRackId(long id) {
            this.id = id
        }

        static ContainerRackId of(long id) {
            return new ContainerRackId(id);
        }
    }
}
