package ru.sberbank.convert;

public class ConversionStatus {
    private final String message;
    private final ConversionStatusCode code;

    public ConversionStatus(ConversionStatusCode code, String message) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public ConversionStatusCode getCode() {
        return code;
    }

    public static ConversionStatus SuccessStatus() {
        return new ConversionStatus(ConversionStatusCode.SUCCESS, "");
    }

    public static ConversionStatus ErrorStatus(String message) {
        return new ConversionStatus(ConversionStatusCode.ERROR, message);
    }
}
