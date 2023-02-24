package ru.sberbank;

import com.itextpdf.kernel.pdf.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {

        String DEST = args[1];
        String SRC = args[0];

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));

        List<PdfIndirectReference> pdfIndirectReferencesList = pdfDoc.listIndirectReferences();
        for (PdfIndirectReference ref : pdfIndirectReferencesList) {

            PdfObject pdfObject = pdfDoc.getPdfObject(ref.getObjNumber());
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

        pdfDoc.close();
    }
}