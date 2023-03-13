package ru.sberbank;

import org.junit.jupiter.api.*;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import ru.sberbank.check.CheckResult;
import ru.sberbank.check.CheckSourceFileExist;
import ru.sberbank.convert.*;
import ru.sberbank.params.ConvertParams;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    @Test
    @DisplayName("Test error decode conversion")
    public void errorDecodeConversionTest() {
        String source = SOURCE_DIR + "pdfA-1.pdf";
        String target = RESULT_DIR + "pdfA-1-error-decode-result.pdf";

        try (MockedConstruction<DecoderConvertor> mockConvertor = Mockito.mockConstruction(DecoderConvertor.class, (mock, context) -> {
            when(mock.changeDocument(any())).thenReturn(ConversionStatus.ErrorStatus("Decode error text"));
        })) {

            Assertions.assertFalse(new File(target).exists());

            ConvertParams convertParams = new ConvertParams(new String[]{source, target});
            Converter converter = new Converter(convertParams);

            List<ConversionStatus> conversionStatuses = converter.makeDocument();
            List<ConversionStatus> errorList = conversionStatuses
                    .stream()
                    .filter(it -> it.getCode() == ConversionStatusCode.ERROR)
                    .collect(Collectors.toList());

            Assertions.assertNotNull(errorList);

            boolean decodeError = errorList
                    .stream()
                    .anyMatch(it -> it.getMessage().equals("Decode error text"));
            Assertions.assertTrue(decodeError);
        }
    }

    @Test
    @DisplayName("Test error OCG conversion")
    public void errorOCGConversionTest() {
        String source = SOURCE_DIR + "pdfA-1.pdf";
        String target = RESULT_DIR + "pdfA-1-error-OCG-result.pdf";

        try (MockedConstruction<OCGConvertor> mockConvertor = Mockito.mockConstruction(OCGConvertor.class, (mock, context) -> {
            when(mock.changeDocument(any())).thenReturn(ConversionStatus.ErrorStatus("OCG conversion error text"));
        })) {
            Assertions.assertFalse(new File(target).exists());

            ConvertParams convertParams = new ConvertParams(new String[]{source, target});
            Converter converter = new Converter(convertParams);

            List<ConversionStatus> conversionStatuses = converter.makeDocument();
            List<ConversionStatus> errorList = conversionStatuses
                    .stream()
                    .filter(it -> it.getCode() == ConversionStatusCode.ERROR)
                    .collect(Collectors.toList());

            Assertions.assertNotNull(errorList);

            boolean OCGConversionError = errorList
                    .stream()
                    .anyMatch(it -> it.getMessage().equals("OCG conversion error text"));
            Assertions.assertTrue(OCGConversionError);
        }
    }

    @Test
    @DisplayName("Test error check params")
    public void errorerrorCheckParamsTest() {
        String source = SOURCE_DIR + "pdfA-1.pdf";
        String target = RESULT_DIR + "pdfA-1-error-check-params-result.pdf";

        try (MockedConstruction<CheckSourceFileExist> mockCheck = Mockito.mockConstruction(CheckSourceFileExist.class, (mock, context) -> {
            List<CheckResult> listResultError = new ArrayList<>();
            listResultError.add(CheckResult.Error("error line 1"));
            listResultError.add(CheckResult.Error("error line 2"));
            when(mock.getResult()).thenReturn(listResultError);
        })) {
            Assertions.assertFalse(new File(target).exists());

            ConvertParams convertParams = new ConvertParams(new String[]{source, target});

            RuntimeException error = assertThrows(RuntimeException.class, () -> new Converter(convertParams));
            Assertions.assertEquals("error line 1\nerror line 2", error.getMessage());
        }
    }
}
