package ru.sberbank.convert;

import com.itextpdf.kernel.pdf.PdfDocument;

public interface IConvertItem {
    ConversionStatus changeDocument(PdfDocument document);
}
