package ru.sberbank;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPdfA1aConverter {
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
                () -> PdfA1aConverter.main(null)
        );

        Assertions.assertEquals("The program must be called with parameters", exception.getMessage());
    }

    @Test
    @DisplayName("Test error params exception")
    public void errorParamsExceptionTest() {
        String source = "dummy";
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> PdfA1aConverter.main(new String[]{source, ""})
        );

        Assertions.assertEquals("File dummy was not found", exception.getMessage());
    }

    @Test
    @DisplayName("Test converter")
    public void converterTest() {
        String source = SOURCE_DIR + "pdfA-1.pdf";
        String target = RESULT_DIR + "pdfA-1-result.pdf";

        Assertions.assertFalse(new File(target).exists());
        PdfA1aConverter.main(new String[]{source, target});
        Assertions.assertTrue(new File(target).exists());
    }
}
