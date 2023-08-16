package com.github.ttrojan.rackContainer

class RackTestBuilder {

    static rackBuilder() {
        return new RackTestBuilder()
    }

    RackId rackId = RackId.of(1L)
    ContainerRackId containerRackId = ContainerRackId.of(1L)
    Capacity capacity = Capacity.of(10)

    RackTestBuilder leftShift(Capacity capacity) {
        this.capacity = capacity
        return this
    }

    RackTestBuilder leftShift(RackId rackId) {
        this.rackId = rackId
        return this
    }

    RackTestBuilder leftShift(ContainerRackId containerRackId) {
        this.containerRackId = containerRackId;
        return this
    }

    Rack build() {
        return new Rack(rackId.id,
                containerRackId.id,
                capacity.initialCapacity,
                capacity.leftCapacity,
        )
    }

    static class Capacity {
        int initialCapacity;
        int leftCapacity;

        Capacity(int initialCapacity, int leftCapacity) {
            this.initialCapacity = initialCapacity
            this.leftCapacity = leftCapacity
        }

        static Capacity of(int initialCapacity) {
            return new Capacity(initialCapacity, initialCapacity);
        }

        static Capacity of(int initialCapacity, int leftCapacity) {
            return new Capacity(initialCapacity, leftCapacity);
        }

    }


    static class RackId {
        long id;

        RackId(long id) {
            this.id = id
        }

        static RackId of(long id) {
            return new RackId(id);
        }
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
