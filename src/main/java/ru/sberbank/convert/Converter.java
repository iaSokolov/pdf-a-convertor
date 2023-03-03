package ru.sberbank.convert;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import ru.sberbank.check.CheckParams;
import ru.sberbank.check.CheckResult;
import ru.sberbank.check.CheckResultCode;
import ru.sberbank.params.ConvertParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return list;
    }

    public ConversionStatus makeDocument() throws Exception {
        try {
            PdfReader reader = new PdfReader(this.params.getSourceFilePath());
            PdfWriter writer = new PdfWriter(this.params.getTargetFilePath());

            PdfDocument document = new PdfDocument(reader, writer);

            this.converters.forEach(it -> it.changeDocument(document));

            document.close();

            return ConversionStatus.SuccessStatus();
        } catch (Exception error) {
            return ConversionStatus.ErrorStatus(error.getMessage());
        }
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