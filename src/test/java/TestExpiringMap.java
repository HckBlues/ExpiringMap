import org.joda.time.DateTimeUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.task.ExpiringMap;

class TestExpiringMap {

    private ExpiringMap expiringMap;

    private final Long DEFAULT_START_TIME = 1000L;

    @BeforeEach
    void setup() {
        expiringMap = new ExpiringMap();
        DateTimeUtils.setCurrentMillisFixed(DEFAULT_START_TIME);
    }
    @Test
    void testExpirationMapNormalFlow() {
        expiringMap.put(1, 1, 0);
        expiringMap.put(3, 3, 5);
        expiringMap.put(4, 4, 500);

        DateTimeUtils.setCurrentMillisFixed(DEFAULT_START_TIME + 6);

        Assertions.assertNull(expiringMap.get(1));
        Assertions.assertNull(expiringMap.get(3));
        Assertions.assertEquals(Integer.valueOf(4), expiringMap.get(4));

        expiringMap.put(3, 3, 130);

        DateTimeUtils.setCurrentMillisFixed(DEFAULT_START_TIME + 100);

        Assertions.assertEquals(Integer.valueOf(3), expiringMap.get(3));
        Assertions.assertEquals(Integer.valueOf(4), expiringMap.get(4));

        DateTimeUtils.setCurrentMillisFixed(DEFAULT_START_TIME + 200);

        Assertions.assertNull(expiringMap.get(3));
        Assertions.assertEquals(Integer.valueOf(4), expiringMap.get(4));

        DateTimeUtils.setCurrentMillisFixed(DEFAULT_START_TIME + 1000);
        Assertions.assertNull(expiringMap.get(4));

    }

    @Test
    void testExpirationMapOverrideKey() {
        expiringMap.put(1, 1, 10);
        expiringMap.put(1, 10, 100);
        expiringMap.put(2, 2, 10);

        DateTimeUtils.setCurrentMillisFixed(DEFAULT_START_TIME + 50);

        Assertions.assertEquals(Integer.valueOf(10), expiringMap.get(1));
        Assertions.assertNull(expiringMap.get(2));
    }

    @Test
    void testExpirationMapGetKeyOnExpirationLimitEdge() {
        expiringMap.put(1, 1, 10);

        DateTimeUtils.setCurrentMillisFixed(DEFAULT_START_TIME + 10);

        Assertions.assertEquals(Integer.valueOf(1), expiringMap.get(1));
    }
}
