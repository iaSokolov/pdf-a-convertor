package ru.sberbank.decoder;

import com.itextpdf.kernel.pdf.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class StreamDecoder {
    public PdfDocument decode(PdfDocument document) throws Exception {
        List<PdfIndirectReference> pdfIndirectReferencesList = document.listIndirectReferences();
        for (PdfIndirectReference ref : pdfIndirectReferencesList) {

            PdfObject pdfObject = document.getPdfObject(ref.getObjNumber());
            if (pdfObject != null && pdfObject.getType() == PdfObject.STREAM) {

                boolean isMetadata = false;
                PdfStream pdfStream = (PdfStream) pdfObject;

                PdfObject objectType = pdfStream.get(new PdfName("Type"));
                if (objectType != null) {
                    if (objectType.getType() == PdfObject.NAME) {
                        PdfName valueObjectType = (PdfName) objectType;

                        String value = valueObjectType.getValue();

                        if (Objects.equals(value, "Metadata")) {
                            isMetadata = true;
                        }
                    }
                }
                if (isMetadata) {
                    String value = new String(pdfStream.getBytes(true));
                    pdfStream.setData(value.getBytes(StandardCharsets.UTF_8));
                }
            }
        }
        return document;
    }
}
