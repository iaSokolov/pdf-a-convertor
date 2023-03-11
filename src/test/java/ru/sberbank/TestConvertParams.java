package ru.sberbank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.sberbank.params.ConvertParams;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestConvertParams {
    @Test
    @DisplayName("Test null args exception")
    public void nullArgsExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ConvertParams(null));

        Assertions.assertEquals("The program must be called with parameters", exception.getMessage());
    }

    @Test
    @DisplayName("Test error count args exception")
    public void errorCountArgsExceptionTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new ConvertParams(new String[]{"1"}));

        Assertions.assertEquals("The program was called with incorrect parameters", exception.getMessage());
    }

    @Test
    @DisplayName("Test source file path args")
    public void sourceFilePathTest() {
        Assertions.assertEquals(
                "source",
                new ConvertParams(new String[]{"source", "target"}).getSourceFilePath());
    }

    @Test
    @DisplayName("Test target file path args")
    public void targetFilePathTest() {
        Assertions.assertEquals(
                "target",
                new ConvertParams(new String[]{"source", "target"}).getTargetFilePath());
    }
}
