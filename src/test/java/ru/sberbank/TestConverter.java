package ru.sberbank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.sberbank.params.ConvertParams;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestConverter {
    @Test
    @DisplayName("Test error count args exception")
    public void errorCountArgsExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ConvertParams(new String[]{"1"}));

        Assertions.assertEquals("The program was called with incorrect parameters", exception.getMessage());
    }
}
