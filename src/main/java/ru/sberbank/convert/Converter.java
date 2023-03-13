package ru.sberbank.convert;

import com.itextpdf.kernel.pdf.*;
import ru.sberbank.check.CheckParams;
import ru.sberbank.check.CheckResult;
import ru.sberbank.check.CheckResultCode;
import ru.sberbank.params.ConvertParams;

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

        if (this.params.getConverters().stream().anyMatch(it -> it.equals(IConvertItem.DecoderConvertorParamCode))) {
            list.add(new DecoderConvertor());
        }

        if (this.params.getConverters().stream().anyMatch(it -> it.equals(IConvertItem.OCGConvertorParamCode))) {
            list.add(new OCGConvertor());
        }

        return list;
    }

    public List<ConversionStatus> makeDocument() {
        List<ConversionStatus> result;

        try {
            PdfReader reader = new PdfReader(this.params.getSourceFilePath());
            PdfWriter writer = new PdfWriter(this.params.getTargetFilePath());
            PdfDocument document = new PdfDocument(reader, writer);

            result = this.converters
                    .stream()
                    .map(convertItem -> convertItem.changeDocument(document))
                    .collect(Collectors.toList());

            document.close();
        } catch (Exception error) {
            result = new ArrayList<>();
            String message = error.getMessage();
            result.add(ConversionStatus.ErrorStatus(message));
        }

        return result;
    }

    private void checkParam(ConvertParams params) throws RuntimeException {
        if (params == null) {
            throw new RuntimeException("Params is null");
        }

        List<CheckResult> checkResults = new CheckParams(params).getResult();
        Optional<String> errorMessage = checkResults
                .stream()
                .filter(it -> it.getCode() == CheckResultCode.ERROR)
                .map(CheckResult::getMessage)
                .reduce((prev, item) -> prev + System.lineSeparator() + item);

        if (errorMessage.isPresent()) {
            throw new RuntimeException(errorMessage.get());
        }
    }

    public List<IConvertItem> getConverters() {
        return converters;
    }
}