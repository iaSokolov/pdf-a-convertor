package ru.sberbank;

import org.junit.jupiter.api.*;
import ru.sberbank.convert.ConversionStatus;
import ru.sberbank.convert.ConversionStatusCode;
import ru.sberbank.convert.Converter;
import ru.sberbank.params.ConvertParams;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestConverter {
    private static final String RESULT_DIR = "tmp/";
    private static final String SOURCE_DIR = "src/test/resources/";

    private static void deleteAllFilesInDir(String dir) throws IOException {
        Files.walk(Paths.get(dir))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @BeforeAll
    public static void deleteAllFilesBeforeTest() throws IOException {
        deleteAllFilesInDir(RESULT_DIR);
    }

    @AfterAll
    public static void deleteAllFilesAfterTest() throws IOException {
        deleteAllFilesInDir(RESULT_DIR);
    }

    @Test
    @DisplayName("Test error null params exception")
    public void errorNullParamsExceptionTest() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> new Converter(null)
        );

        Assertions.assertEquals("Params is null", exception.getMessage());
    }

    @Test
    @DisplayName("Test error params exception")
    public void errorParamsExceptionTest() {
        String source = "dummy";
        ConvertParams convertParams = new ConvertParams(new String[]{source, ""});
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> new Converter(convertParams)
        );

        Assertions.assertEquals("File dummy was not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test count converters")
    public void countConvertersTest() {
        String source = SOURCE_DIR + "pdf-standard.pdf";
        String target = "";

        ConvertParams convertParams = new ConvertParams(new String[]{source, target});
        Converter converter = new Converter(convertParams);

        Assertions.assertNotNull(converter.getConverters());
        Assertions.assertEquals(2, converter.getConverters().size());
    }

    @Test
    @DisplayName("Test converter")
    public void converterTest() {
        String source = SOURCE_DIR + "pdfA-1.pdf";
        String target = RESULT_DIR + "pdfA-1-result.pdf";

        Assertions.assertFalse(new File(target).exists());

        ConvertParams convertParams = new ConvertParams(new String[]{source, target});
        Converter converter = new Converter(convertParams);

        List<ConversionStatus> conversionStatuses = converter.makeDocument();
        boolean hasError = conversionStatuses
                .stream()
                .anyMatch(it -> it.getCode() == ConversionStatusCode.ERROR);
        Assertions.assertFalse(hasError);

        Assertions.assertTrue(new File(target).exists());
    }
}
