package backend.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RoleUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RoleUser getRoleUserSample1() {
        return new RoleUser().id(1L);
    }

    public static RoleUser getRoleUserSample2() {
        return new RoleUser().id(2L);
    }

    public static RoleUser getRoleUserRandomSampleGenerator() {
        return new RoleUser().id(longCount.incrementAndGet());
    }
}
