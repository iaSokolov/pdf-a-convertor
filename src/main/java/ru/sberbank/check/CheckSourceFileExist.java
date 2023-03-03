package ru.sberbank.check;

import ru.sberbank.params.ConvertParams;

public class CheckSourceFileExist implements ICheckItem {
    private CheckResult result;

    private final ConvertParams params;


    public CheckSourceFileExist(ConvertParams params) {
        this.params = params;
    }

    @Override
    public CheckResult getResult() {
        if (this.result == null) {
            this.result = this.check(this.params);
        }
        return this.result;
    }

    private CheckResult check(ConvertParams params) {
        return CheckResult.Success();
    }
}
