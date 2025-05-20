package io.joshuasalcedo.pretty.core.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrettySystemTest {


    @Test
    void testPrettySystem() {
      assertThrows(NullPointerException.class, () -> new PrettySystem());
    }

}