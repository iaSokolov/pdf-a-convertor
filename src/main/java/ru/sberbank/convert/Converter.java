package ru.sberbank.convert;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import ru.sberbank.decoder.StreamDecoder;
import ru.sberbank.params.ConvertParams;

public class Converter {
    private final ConvertParams params;

    public Converter(ConvertParams params) {
        this.params = params;
    }

    public ConversionStatus makeDocument() throws Exception {
        try {
            PdfReader reader = new PdfReader(this.params.getSourceFilePath());
            PdfWriter writer = new PdfWriter(this.params.getTargetFilePath());
            PdfDocument document = new PdfDocument(reader, writer);

            new StreamDecoder(document).decode();

            document.close();

            return ConversionStatus.SuccessStatus();
        }
        catch (Exception error) {
            return ConversionStatus.ErrorStatus(error.getMessage());
        }
    }
}