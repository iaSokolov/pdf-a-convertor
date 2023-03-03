package ru.sberbank.check;

public class CheckResult {
    private final CheckResultCode code;
    private final String message;

    public CheckResult(CheckResultCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public CheckResultCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static CheckResult Error(String message) {
        return new CheckResult(CheckResultCode.ERROR, message);
    }

    public static CheckResult Success() {
        return new CheckResult(CheckResultCode.SUCCESS, "");
    }
}
