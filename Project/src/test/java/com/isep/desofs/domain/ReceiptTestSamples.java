package com.isep.desofs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReceiptTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Receipt getReceiptSample1() {
        return new Receipt().id(1L).idPayment(1L).description("description1");
    }

    public static Receipt getReceiptSample2() {
        return new Receipt().id(2L).idPayment(2L).description("description2");
    }

    public static Receipt getReceiptRandomSampleGenerator() {
        return new Receipt()
            .id(longCount.incrementAndGet())
            .idPayment(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString());
    }
}
