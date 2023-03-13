package ru.sberbank.convert;

import com.itextpdf.kernel.pdf.PdfDocument;

public interface IConvertItem {
    String ConvertorParamCode = "-c";
    String OCGConvertorParamCode = "-cOCG";
    String DecoderConvertorParamCode = "-cDecoder";

    ConversionStatus changeDocument(PdfDocument document);
}
