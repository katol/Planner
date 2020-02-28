import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Тестирует основной класс приложения
 * @author Анатолий Берелехис
 */
public class PlannerTest {

    /**
     * Проверяет, что planTask возвращает правильный результат
     */
    @Test
    public void planTasks_ReturnsCorrectList() {
        List<List<Integer>> expected = new LinkedList<>();
        expected.add(Arrays.asList(0, 2));
        expected.add(Arrays.asList(11, 13));
        expected.add(Collections.singletonList(12));
        expected.add(Arrays.asList(22, 21));
        expected.add(Collections.singletonList(31));

        List<List<Integer>> actual = new Planner().planTasks("data/data1.txt");

        boolean areEqual = true;
        if (actual.size() != expected.size())
            areEqual = false;
        for (int i = 0; i < actual.size(); i++) {
            List<Integer> expectedList = expected.get(i);
            List<Integer> actualList = actual.get(i);
            if (!actualList.containsAll(expectedList) || actualList.size() != expectedList.size())
                areEqual = false;
        }
        assertTrue(areEqual);
    }

    /**
     * Проверяет, что planTask возвращает результат за разумное время на большом объеме данных
     */
    @Test(timeout = 500000)
    public void planTasks_WorksFast() {
        new Planner().planTasks("data/data2.txt");
    }

    /**
     * Проверяет, что planTask возвращает результат за одну минуту на большом объеме данных
     *      с ограничением объема кучи 1 гигабайт
     * Ограничение объема кучи для этого теста выставлено через Intellij Idea
     */
    @Test(timeout = 60000)
    public void planTasks_WorksVeryFast_WithHeapLimit() {
        new Planner().planTasks("data/data2.txt");
    }
}