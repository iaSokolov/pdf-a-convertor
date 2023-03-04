package ru.sberbank.convert;

import com.itextpdf.kernel.pdf.PdfDocument;

public interface IConvertItem {
    void changeDocument(PdfDocument document) throws Exception;
}
