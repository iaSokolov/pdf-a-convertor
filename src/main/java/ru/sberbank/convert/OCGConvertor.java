package ru.sberbank.convert;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;

public class OCGConvertor implements IConvertItem {
    @Override
    public ConversionStatus changeDocument(PdfDocument document) {
        ConversionStatus result;
        try {
            document.getCatalog().remove(PdfName.OCProperties);

            result = ConversionStatus.SuccessStatus();
        } catch (Exception e) {
            result = ConversionStatus.ErrorStatus(e.getMessage());
        }
        return result;
    }
}