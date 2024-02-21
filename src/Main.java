import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {

    static AtomicInteger three = new AtomicInteger(0);
    static AtomicInteger four = new AtomicInteger(0);
    static AtomicInteger five = new AtomicInteger(0);

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>(3);

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread match = new Thread(() -> {
            for (String str : texts) {
                if (str.length() == 3 && str.matches("^(.)\\1+$")) {
                    three.getAndIncrement();
                }
            }
        });
        threads.add(match);
        match.start();

        Thread palindrome = new Thread(() -> {
            for (String str : texts) {
                if (str.length() == 4 && isPalindrome(str)) {
                    four.getAndIncrement();
                }
            }
        });
        threads.add(palindrome);
        palindrome.start();

        Thread ascending = new Thread(() -> {
            for (String str : texts) {
                if (str.length() == 5 && isAscending(str)) {
                    five.getAndIncrement();
                }
            }
        });
        threads.add(ascending);
        ascending.start();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Красивых слов с длиной 3: " + three + " шт");
        System.out.println("Красивых слов с длиной 4: " + four + " шт");
        System.out.println("Красивых слов с длиной 5: " + five + " шт");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean isPalindrome(String str) {
        return IntStream.range(0, str.length() / 2)
                .allMatch(i -> str.charAt(i) == str.charAt(str.length() - i - 1));
    }

    public static boolean isAscending(String str) {
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) < str.charAt(i - 1))
                return false;
        }
        return true;
    }
}