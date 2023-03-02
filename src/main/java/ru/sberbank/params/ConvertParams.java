package ru.sberbank.params;

public class ConvertParams {
    private final String sourceFilePath;
    private final String targetFilePath;

    public ConvertParams(String[] args) throws IllegalArgumentException {
        if (args == null) {
            throw new IllegalArgumentException("The program must be called with parameters");
        } else if (args.length != 2) {
            throw new IllegalArgumentException("The program was called with incorrect parameters");
        }
        this.targetFilePath = args[1];
        this.sourceFilePath = args[0];
    }

    public String getSourceFilePath() {
        return this.sourceFilePath;
    }

    public String getTargetFilePath() {
        return this.targetFilePath;
    }
}
