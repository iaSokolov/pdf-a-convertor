package ru.sberbank;

import ru.sberbank.convert.ConversionStatus;
import ru.sberbank.convert.Converter;
import ru.sberbank.params.ConvertParams;

public class PdfA1aConverter {
    public static void main(String[] args) throws Exception {
        ConversionStatus conversionStatus = new Converter(new ConvertParams(args)).makeDocument();

        switch (conversionStatus.getCode()) {
            case SUCCESS:
                break;
            case ERROR:
                throw new RuntimeException(conversionStatus.getMessage());
        }
    }
}