package ru.sberbank.check;

import ru.sberbank.params.ConvertParams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CheckParams {
    private List<CheckResult> result;
    private final ConvertParams params;
    private final List<ICheckItem> checks;

    public CheckParams(ConvertParams params) {
        this.params = params;
        this.checks = this.initCheck();
    }

    private List<ICheckItem> initCheck() {
        ArrayList<ICheckItem> checks = new ArrayList<>();
        checks.add(new CheckSourceFileExist(params));
        return checks;
    }

    public List<CheckResult> getResult() {
        if (this.result == null) {
            this.result = this.check();
        }
        return this.result;
    }

    private List<CheckResult> check() {
        return checks
                .stream()
                .map(ICheckItem::getResult)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<ICheckItem> getChecks() {
        return checks;
    }
}
