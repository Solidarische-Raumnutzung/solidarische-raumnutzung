package edu.kit.hci.soli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SoliApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testAlwaysTrue() {
        Assertions.assertTrue(true);
    }

    @Test
    void testAlwaysFalse() {
        Assertions.fail();
    }
}
