package com.isep.desofs.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static User getUserSample1() {
        return new User().id(1L).email("email1").password("password1").address("address1").contact("contact1");
    }

    public static User getUserSample2() {
        return new User().id(2L).email("email2").password("password2").address("address2").contact("contact2");
    }

    public static User getUserRandomSampleGenerator() {
        return new User()
            .id(longCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .contact(UUID.randomUUID().toString());
    }
}
