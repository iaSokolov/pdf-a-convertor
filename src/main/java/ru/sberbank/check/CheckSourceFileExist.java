package ru.sberbank.check;

import ru.sberbank.params.ConvertParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckSourceFileExist implements ICheckItem {
    private List<CheckResult> result;

    private final ConvertParams params;

    public CheckSourceFileExist(ConvertParams params) {
        this.params = params;
    }

    @Override
    public List<CheckResult> getResult() {
        if (this.result == null) {
            this.result = new ArrayList<>();
            this.result.add(this.checkFile(this.params.getSourceFilePath()));
        }
        return this.result;
    }

    private CheckResult checkFile(String path) {
        File file = new File(path);

        if (file.exists() && !file.isDirectory()) {
            return CheckResult.Success();
        } else {
            return CheckResult.Error(String.join(" ", "File", path, "was not found"));
        }
    }
}
