package es.upm.pproject.sokoban;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MainTest {

    @Test
    void testMainMethodRunsWithoutException() {
        assertDoesNotThrow(() -> Main.main(new String[]{}), "Main method should run without throwing exceptions");
    }
}
