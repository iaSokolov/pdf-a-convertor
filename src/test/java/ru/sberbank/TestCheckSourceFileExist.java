package ru.sberbank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.sberbank.check.CheckResult;
import ru.sberbank.check.CheckResultCode;
import ru.sberbank.check.CheckSourceFileExist;
import ru.sberbank.params.ConvertParams;

import java.util.List;

public class TestCheckSourceFileExist {
    @Test
    @DisplayName("Test file not exist")
    public void fileNotExistTest() {
        ConvertParams convertParams = new ConvertParams(new String[]{"1", ""});
        CheckSourceFileExist checkSourceFileExist = new CheckSourceFileExist(convertParams);

        //повтор два раза, для тестирования кеширования getResult
        for (int count = 0; count <= 1; count++) {
            List<CheckResult> result = checkSourceFileExist.getResult();

            CheckResult errors = result
                    .stream()
                    .filter(it -> it.getCode() == CheckResultCode.ERROR)
                    .findFirst()
                    .get();

            Assertions.assertNotNull(errors);
            Assertions.assertEquals("File 1 was not found", errors.getMessage());
        }
    }

    @Test
    @DisplayName("Test file exist")
    public void fileExistTest() {
        ConvertParams convertParams = new ConvertParams(new String[]{"src/test/resources/pdf-standard.pdf", ""});
        List<CheckResult> result = new CheckSourceFileExist(convertParams).getResult();

        CheckResult errors = result
                .stream()
                .filter(it -> it.getCode() == CheckResultCode.SUCCESS)
                .findFirst()
                .get();

        Assertions.assertNotNull(errors);
    }

    @Test
    @DisplayName("Test file is directory")
    public void fileIsDirectoryErrorTest() {
        ConvertParams convertParams = new ConvertParams(new String[]{"src/test/resources", ""});
        List<CheckResult> result = new CheckSourceFileExist(convertParams).getResult();

        CheckResult errors = result
                .stream()
                .filter(it -> it.getCode() == CheckResultCode.ERROR)
                .findFirst()
                .get();

        Assertions.assertNotNull(errors);
        Assertions.assertEquals("File src/test/resources was not found", errors.getMessage());
    }
}
