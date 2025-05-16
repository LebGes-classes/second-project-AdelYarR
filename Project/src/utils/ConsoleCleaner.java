package utils;

// Вспомогательный класс с методом очистки консоли.
public class ConsoleCleaner {
    public static void clear() {
        for (int i = 0; i < 25; i++) {
            System.out.println();
        }
    }
}
