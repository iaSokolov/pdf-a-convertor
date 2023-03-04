package ru.sberbank.convert;

import com.itextpdf.kernel.pdf.PdfDocument;
import ru.sberbank.decoder.StreamDecoder;

public class DecoderConvertor implements IConvertItem {
    @Override
    public void changeDocument(PdfDocument document) throws Exception {
        new StreamDecoder().decode(document);
    }
}
