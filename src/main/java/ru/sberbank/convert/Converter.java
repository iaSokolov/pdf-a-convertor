package ru.sberbank.convert;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.pdfa.PdfADocument;
import ru.sberbank.check.CheckParams;
import ru.sberbank.check.CheckResult;
import ru.sberbank.check.CheckResultCode;
import ru.sberbank.params.ConvertParams;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Converter {
    private final ConvertParams params;
    private final List<IConvertItem> converters;

    public Converter(ConvertParams params) throws RuntimeException {
        this.checkParam(params);
        this.params = params;
        this.converters = this.initConverters();
    }

    private List<IConvertItem> initConverters() {
        ArrayList<IConvertItem> list = new ArrayList<>();

        list.add(new DecoderConvertor());
        list.add(new OCGConvertor());

        return list;
    }

    public List<ConversionStatus> makeDocument() {
        List<ConversionStatus> result;

        try {
            FileInputStream colorProfile = new FileInputStream("src/main/resources/color/sRGB_CS_profile.icm");

            PdfADocument pdf = new PdfADocument(new PdfWriter(this.params.getTargetFilePath()),
                    PdfAConformanceLevel.PDF_A_1A,
                    new PdfOutputIntent(
                            "Custom",
                            "",
                            "http://www.color.org",
                            "sRGB IEC61966-2.1",
                            colorProfile));

            Document document = new Document(pdf);

            //Setting some required parameters ???
            pdf.setTagged();

            PdfReader reader = new PdfReader(this.params.getSourceFilePath());
            PdfDocument sourceDocument = new PdfDocument(reader);
            sourceDocument.copyPagesTo(1, 2, pdf);

            result = new ArrayList<>();
            document.close();
        } catch (Exception error) {
            result = new ArrayList<>();
            result.add(ConversionStatus.ErrorStatus(error.getMessage()));
        }

        return result;
    }

    private void checkParam(ConvertParams params) throws RuntimeException {
        List<CheckResult> checkResults = new CheckParams(params).getResult();
        Optional<String> errorMessage = checkResults
                .stream()
                .filter(it -> it.getCode() == CheckResultCode.ERROR)
                .map(CheckResult::getMessage)
                .reduce((p, i) -> p + i + "\n");

        if (errorMessage.isPresent()) {
            throw new RuntimeException(errorMessage.get());
        }
    }
}