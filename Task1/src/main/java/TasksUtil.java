import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TasksUtil {

    //Task1: Поиск четных чисел
    public List<Integer> findEvenNumbers(List<Integer> givenList) {
        return givenList
                .stream()
                .filter(elem -> (elem & 1) != 1)
                .collect(Collectors.toList());
    }

    //Task1: Поиск нечетных чисел
    public List<Integer> findOddNumbers(List<Integer> givenList) {
        return givenList
                .stream()
                .filter(elem -> (elem & 1) == 1)
                .collect(Collectors.toList());
    }

    // Task2,3: Поиск чисел, которые делятся на 3,9,5,10
    public List<Integer> findNumbersDivisibleBy(List<Integer> givenList, final int divider) {
        return givenList
                .stream()
                .filter(elem -> elem % divider == 0)
                .collect(Collectors.toList());
    }

    // Task4: Найти НОД
    public int findGcd(List<Integer> givenList) {
        return givenList
                .stream()
                .reduce(givenList.get(0), this::gcd);
    }

    private int gcd(int value1, int value2) {
        int temp;
        while (value2 != 0) {
            temp = value2;
            value2 = value1 % value2;
            value1 = temp;
        }
        return value1;
    }

    // Task4: Найти НОК
    public int findLcm(List<Integer> givenList) {
        return givenList
                .stream()
                .reduce(givenList.get(0), this::lcm);
    }

    private int lcm(int value1, int value2) {
        return value1 * value2 / gcd(value1, value2);
    }

    //Task5: Найти простые числа
    public List<Integer> findPrimeNumbers(List<Integer> givenList) {
        return givenList
                .stream()
                .filter(this::isPrimeNumber)
                .collect(Collectors.toList());
    }

    private boolean isPrimeNumber(final int value) {
        if (value <= 1) {
            return false;
        }
        return IntStream
                .rangeClosed(2, (int) Math.sqrt(value))
                .noneMatch(i -> value % i == 0);
    }

    //Task6: Найти счастливые числа
    public List<Integer> findHappyNumbers(List<Integer> givenList) {
        return givenList
                .stream()
                .filter(this::isHappyNumber)
                .collect(Collectors.toList());
    }

    private boolean isHappyNumber(int value) {
        boolean isHappy = true;
        if (value <= 0) {
            return false;
        }
        int result = value;
        while (result != 1) {
            int[] digits = IntStream
                    .iterate(result, i -> i > 0, i -> i / 10)
                    .map(i -> i % 10)
                    .toArray();
            result = Arrays
                    .stream(digits)
                    .reduce(0, (sum, elem) -> sum + elem * elem);
            if (result == 4) {
                isHappy = false;
                break;
            }
        }
        return isHappy;
    }

    //Task7: Поиск чисел Фибонначи
    public List<Integer> findFibonacciNumbers(List<Integer> givenList) {
        return givenList
                .stream()
                .filter((elem) -> isFibonacciNumber(elem) && elem >= 0)
                .collect(Collectors.toList());
    }


    private boolean isPerfectSquare(int value) {
        int s = (int) Math.sqrt(value);
        return (s * s == value);
    }


    private boolean isFibonacciNumber(int value) {
        return isPerfectSquare(5 * value * value + 4) ||
                isPerfectSquare(5 * value * value - 4);
    }

    //Task8: Поиск чисел-палиндромов
    public List<Integer> findPalindromeNumbers(List<Integer> givenList) {
        return givenList
                .stream()
                .filter(this::isPalindromeNumber)
                .collect(Collectors.toList());
    }

    private boolean isPalindromeNumber(int value) {
        String numberString = String.valueOf(value);
        return numberString.equals(new StringBuilder(numberString).reverse().toString());
    }

    // Task9:
    // Найти период десятичной дроби p = m/n для первых двух целых
    // положительных чисел n и m, расположенных подряд.
    public String getPeriodForFirstTwoPositives(List<Integer> givenList) {
        OptionalInt index = IntStream
                .range(0, givenList.size())
                .filter(i -> givenList.get(i) > 0 && givenList.get(i + 1) > 0)
                .findFirst();
        int firstNumber = givenList.get(index.orElseThrow(IllegalArgumentException::new));
        int secondNumber = givenList.get(index.orElseThrow(IllegalArgumentException::new) + 1);
        return getPeriod(secondNumber, firstNumber);
    }


    private String getPeriod(int value1, int value2) {
        StringBuilder sb = new StringBuilder();
        List<Integer> list = new ArrayList<>();
        int remainder = value1 % value2;
        while (remainder != 0) {
            int tempRemainder = remainder;
            list.add(tempRemainder);
            tempRemainder *= 10;
            sb.append(tempRemainder / value2);
            remainder = tempRemainder % value2;
            if (list.contains(remainder)) {
                return sb.toString();
            }
        }
        return "No period";
    }

    //Task10: Построить треугольник Паскаля для первого положительного числа
    public List<List<Integer>> buildPascalTriangle(List<Integer> givenList, int rowsCount) {
        Optional<Integer> firstPositiveNumber = givenList
                .stream()
                .filter(elem -> elem > 0)
                .findFirst();
        List<Integer> newList = new ArrayList<>();
        newList.add(firstPositiveNumber.orElseThrow(IllegalArgumentException::new));
        List<List<Integer>> triangle = new ArrayList<>();
        for (int i = 1; i <= rowsCount; i++) {
            triangle.add(newList);
            newList = new ArrayList<>(newList);
            newList.add(0, 0);
            for (int j = 0; j < newList.size() - 1; j++) {
                newList.set(j, newList.get(j + 1) + newList.get(j));
            }
        }
        return triangle;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter n->");
        int n = sc.nextInt();
        List<Integer> list = new ArrayList<>();
        System.out.print("\nEnter elements->");
        for (int i = 0; i < n; i++) {
            list.add(sc.nextInt());
        }
        TasksUtil tasks = new TasksUtil();
        System.out.println("Even numbers: " + tasks.findEvenNumbers(list));
        System.out.println("Odd numbers: " + tasks.findOddNumbers(list));
        System.out.println("Numbers divisible by 3: " + tasks.findNumbersDivisibleBy(list, 3));
        System.out.println("Numbers divisible by 5: " + tasks.findNumbersDivisibleBy(list, 5));
        System.out.println("GCD: " + tasks.findGcd(list));
        System.out.println("LCM:  " + tasks.findLcm(list));
        System.out.println("Prime numbers: " + tasks.findPrimeNumbers(list));
        System.out.println("Happy numbers: " + tasks.findHappyNumbers(list));
        System.out.println("Fibonacci numbers: " + tasks.findFibonacciNumbers(list));
        System.out.println("Palindrome numbers: " + tasks.findPalindromeNumbers(list));
        System.out.println("Period: " + tasks.getPeriodForFirstTwoPositives(list));
        System.out.println("PascalTriangle: ");
        tasks.buildPascalTriangle(list, 7).forEach(System.out::println);
    }


}