package ru.sberbank;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.sberbank.check.CheckParams;
import ru.sberbank.check.CheckResult;
import ru.sberbank.check.ICheckItem;
import ru.sberbank.params.ConvertParams;

import java.util.List;

public class TestCheckParams {

    @Test
    @DisplayName("Test count checks")
    public void checksCountTest() {
        CheckParams checkParams = new CheckParams(new ConvertParams(new String[]{"", ""}));
        List<ICheckItem> checks = checkParams.getChecks();

        Assertions.assertNotNull(checks);
        Assertions.assertEquals(1, checks.size());
    }

    @Test
    @DisplayName("Test checks")
    public void checksTest() {
        CheckParams checkParams = new CheckParams(new ConvertParams(new String[]{"1", "2"}));
        //повтор два раза, для тестирования кеширования getResult
        for (int count = 0; count <= 1; count++) {
            List<CheckResult> result = checkParams.getResult();

            Assertions.assertNotNull(result);
            Assertions.assertEquals(1, result.size());
        }
    }
}
