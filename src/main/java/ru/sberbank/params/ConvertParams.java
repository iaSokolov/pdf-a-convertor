package ru.sberbank.params;

import ru.sberbank.convert.IConvertItem;

import java.util.ArrayList;
import java.util.List;

public class ConvertParams {
    private final String sourceFilePath;
    private final String targetFilePath;

    private final List<String> converters = new ArrayList<>();

    public ConvertParams(String[] args) throws IllegalArgumentException {
        if (args == null) {
            throw new IllegalArgumentException("The program must be called with parameters");
        } else if (args.length < 2) {
            throw new IllegalArgumentException("The program was called with incorrect parameters");
        }

        this.sourceFilePath = args[0];
        this.targetFilePath = args[1];

        this.initConverters(args);
    }

    private void initConverters(String[] args) {
        for (int item = 2; item <= args.length - 1; item++) {
            if (args[item].contains(IConvertItem.ConvertorParamCode)) {
                this.converters.add(args[item]);
            }
        }

        //если конвертеры не заданы в параметрах, то берем все конвертеры
        if (this.converters.isEmpty()) {
            this.converters.add(IConvertItem.DecoderConvertorParamCode);
            this.converters.add(IConvertItem.OCGConvertorParamCode);
        }
    }

    public String getSourceFilePath() {
        return this.sourceFilePath;
    }

    public String getTargetFilePath() {
        return this.targetFilePath;
    }

    public List<String> getConverters() {
        return converters;
    }
}