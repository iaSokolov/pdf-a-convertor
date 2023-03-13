package ru.sberbank;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import ru.sberbank.convert.ConversionStatus;
import ru.sberbank.convert.ConversionStatusCode;
import ru.sberbank.convert.OCGConvertor;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TestOCGConvertor {
    private static final String SOURCE_DIR = "src/test/resources/";

    @Test
    @DisplayName("Test error conversion")
    public void errorConversionTest() throws IOException {
        String source = SOURCE_DIR + "pdfA-1.pdf";

        try (MockedConstruction<PdfDocument> mock = Mockito.mockConstruction(PdfDocument.class, (document, context) -> {
            when(document.getCatalog()).thenThrow(new RuntimeException("error"));
        })) {
            PdfDocument document = new PdfDocument(new PdfReader(source));
            ConversionStatus conversionStatus = new OCGConvertor().changeDocument(document);
            Assertions.assertNotNull(conversionStatus);
            Assertions.assertEquals(ConversionStatusCode.ERROR, conversionStatus.getCode());
            Assertions.assertEquals("error", conversionStatus.getMessage());
        }
    }
}
