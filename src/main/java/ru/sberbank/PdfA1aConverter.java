package ru.sberbank;

import ru.sberbank.convert.ConversionStatus;
import ru.sberbank.convert.ConversionStatusCode;
import ru.sberbank.convert.Converter;
import ru.sberbank.params.ConvertParams;

import java.util.Optional;

public class PdfA1aConverter {
    public static void main(String[] args) {
        ConversionStatus conversionStatus = convert(args);

        switch (conversionStatus.getCode()) {
            case SUCCESS:
                break;
            case ERROR:
                throw new RuntimeException(conversionStatus.getMessage());
        }
    }

    private static ConversionStatus convert(String[] args) {
        Converter converter = new Converter(new ConvertParams(args));

        Optional<ConversionStatus> conversionStatus = converter
                .makeDocument()
                .stream()
                .filter(status -> status.getCode() == ConversionStatusCode.ERROR)
                .reduce((p, item) -> ConversionStatus.ErrorStatus(p + item.getMessage()));

        return conversionStatus.orElseGet(ConversionStatus::SuccessStatus);
    }
}