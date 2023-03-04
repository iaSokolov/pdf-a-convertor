package ru.sberbank.convert;

import com.itextpdf.kernel.pdf.PdfDocument;
import ru.sberbank.decoder.StreamDecoder;

public class DecoderConvertor implements IConvertItem {
    @Override
    public ConversionStatus changeDocument(PdfDocument document) {
        ConversionStatus result;
        try {
            new StreamDecoder().decode(document);

            result = ConversionStatus.SuccessStatus();
        } catch (Exception e) {
            result = ConversionStatus.ErrorStatus(e.getMessage());
        }
        return result;
    }
}