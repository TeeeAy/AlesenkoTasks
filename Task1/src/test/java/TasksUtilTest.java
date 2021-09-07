import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class TasksUtilTest {

    private final List<Integer> testList = new ArrayList<>();
    private final TasksUtil tasks = new TasksUtil();

    @BeforeEach
    void setUp() {
        for (int i = -6; i < 33; i++) {
            testList.add(i);
        }
    }

    @Test
    void testFindEvenNumbers() {
        List<Integer> expected = Arrays.asList(2, 4, 6, 8, 10, 12, 14
                , 16, 18, 20, 22, 24, 26, 28, 30, 32);
        List<Integer> actual = tasks.findEvenNumbers(testList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindOddNumbers() {
        List<Integer> expected = Arrays.asList(1, 3, 5, 7, 9, 11, 13
                , 15, 17, 19, 21, 23, 25, 27, 29, 31);
        List<Integer> actual = tasks.findOddNumbers(testList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindNumbersDivisibleBy3() {
        List<Integer> expected = Arrays.asList(-6, -3, 0, 3, 6, 9
                , 12, 15, 18, 21, 24, 27, 30);
        List<Integer> actual = tasks.findNumbersDivisibleBy(testList, 3);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindNumbersDivisibleBy5() {
        List<Integer> expected = Arrays.asList(-5, 0, 5, 10, 15, 20, 25, 30);
        List<Integer> actual = tasks.findNumbersDivisibleBy(testList, 5);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindGcd() {
        int expected = 5;
        int actual = tasks.findGcd(Arrays.asList(35, 55, 45));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindLcm() {
        int expected = 48048;
        int actual = tasks.findLcm(Arrays.asList(84, 6, 48, 7, 143));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindPrimeNumbers() {
        List<Integer> expected = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31);
        List<Integer> actual = tasks.findPrimeNumbers(testList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindHappyNumbers() {
        List<Integer> expected = Arrays.asList(1, 7, 10, 13, 19, 23, 28, 31, 32);
        List<Integer> actual = tasks.findHappyNumbers(testList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindFibonacciNumbers() {
        List<Integer> expected = Arrays.asList(0, 1, 2, 3, 5, 8, 13, 21);
        List<Integer> actual = tasks.findFibonacciNumbers(testList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindPalindromeNumbers() {
        List<Integer> expected = Arrays.asList(1, 11111111, 234432);
        List<Integer> actual = tasks.findPalindromeNumbers(Arrays.asList(-12, 1,
                11111111, 234432, 839, 3321));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetPeriodForFirstTwoPositives() {
        String expected = "076923";
        String actual = tasks.getPeriodForFirstTwoPositives(Arrays.asList(13, 1, 22));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testGetPeriodForFirstTwoPositivesNoPeriod() {
        String expected = "No period";
        String actual = tasks.getPeriodForFirstTwoPositives(Arrays.asList(2, 1, 22));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testBuildPascalTriangle() {
        List<List<Integer>> expected = new ArrayList<>();
        expected.add(Arrays.asList(1));
        expected.add(Arrays.asList(1,1));
        expected.add(Arrays.asList(1,2,1));
        expected.add(Arrays.asList(1,3,3,1));
        expected.add(Arrays.asList(1,4,6,4,1));
        expected.add(Arrays.asList(1,5,10,10,5,1));
        expected.add(Arrays.asList(1,6,15,20,15,6,1));
        List<List<Integer>> actual = tasks.buildPascalTriangle(testList,7);
        Assertions.assertEquals(expected, actual);
    }
}
