package ru.sberbank.check;

import org.jetbrains.annotations.NotNull;
import ru.sberbank.params.ConvertParams;

import java.util.ArrayList;
import java.util.List;

public class CheckParams {
    private List<CheckResult> result;
    private final ConvertParams params;
    private final List<ICheckItem> checks;


    public CheckParams(ConvertParams params) {
        this.params = params;
        this.checks = this.initCheck();
    }

    private @NotNull List<ICheckItem> initCheck() {
        ArrayList<ICheckItem> checks = new ArrayList<>();
        checks.add(new CheckSourceFileExist(params));
        return checks;
    }

    public List<CheckResult> getResult() {
        if (this.result == null) {
            this.result = this.check(this.params);
        }
        return this.result;
    }

    private List<CheckResult> check(ConvertParams params) {
        return (List<CheckResult>) checks.stream().map(it -> it.getResult());
    }
}
