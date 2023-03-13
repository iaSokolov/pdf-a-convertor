package ru.sberbank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.sberbank.convert.IConvertItem;
import ru.sberbank.params.ConvertParams;

import java.util.List;

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

    @Test
    @DisplayName("Test default converters")
    public void defaultConvertersTest() {
        List<String> converters = new ConvertParams(new String[]{"source", "target"}).getConverters();
        Assertions.assertNotNull(converters);
        Assertions.assertEquals(2, converters.size());
        Assertions.assertTrue(converters.stream().anyMatch(it -> it.equals(IConvertItem.OCGConvertorParamCode)));
        Assertions.assertTrue(converters.stream().anyMatch(it -> it.equals(IConvertItem.DecoderConvertorParamCode)));
    }

    @Test
    @DisplayName("Test OCGConvertor in param")
    public void OCGConvertorPramTest() {
        List<String> converters = new ConvertParams(new String[]{"source", "target", "-cOCG"}).getConverters();
        Assertions.assertNotNull(converters);
        Assertions.assertEquals(1, converters.size());
        Assertions.assertTrue(converters.stream().anyMatch(it -> it.equals(IConvertItem.OCGConvertorParamCode)));
    }

    @Test
    @DisplayName("Test decoderConvertor in param")
    public void decoderConvertorPramTest() {
        List<String> converters = new ConvertParams(new String[]{"source", "target", "-cDecoder"}).getConverters();
        Assertions.assertNotNull(converters);
        Assertions.assertEquals(1, converters.size());
        Assertions.assertTrue(converters.stream().anyMatch(it -> it.equals(IConvertItem.DecoderConvertorParamCode)));
    }

    @Test
    @DisplayName("Test ignore other param value")
    public void ignoreOtherParamTest() {
        List<String> converters = new ConvertParams(new String[]{"source", "target", "-a", "test", "-cDecoder"}).getConverters();
        Assertions.assertNotNull(converters);
        Assertions.assertEquals(1, converters.size());
        Assertions.assertTrue(converters.stream().anyMatch(it -> it.equals(IConvertItem.DecoderConvertorParamCode)));
    }
}
