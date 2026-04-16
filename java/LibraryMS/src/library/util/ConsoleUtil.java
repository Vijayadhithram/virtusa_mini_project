package library.util;

import java.util.Scanner;

public class ConsoleUtil {
    private static final Scanner sc = new Scanner(System.in);
    private static final int WIDTH = 62;

    public static void header(String title) {
        System.out.println();
        line('=');
        int pad = (WIDTH - title.length()) / 2;
        System.out.println(" ".repeat(Math.max(0, pad)) + title);
        line('=');
    }

    public static void section(String title) {
        System.out.println();
        System.out.println("  -- " + title + " --");
        line('-');
    }

    public static void line(char c) {
        System.out.println(String.valueOf(c).repeat(WIDTH));
    }

    public static void line() { line('-'); }

    public static void msg(String text) {
        System.out.println("  " + text);
    }

    public static void success(String text) {
        System.out.println("  [OK] " + text);
    }

    public static void error(String text) {
        System.out.println("  [ERROR] " + text);
    }

    public static void warn(String text) {
        System.out.println("  [!] " + text);
    }

    public static String prompt(String label) {
        System.out.print("  " + label + ": ");
        return sc.nextLine().trim();
    }

    public static String promptOptional(String label) {
        System.out.print("  " + label + " (press Enter to skip): ");
        return sc.nextLine().trim();
    }

    public static int promptInt(String label, int min, int max) {
        while (true) {
            String input = prompt(label + " [" + min + "-" + max + "]");
            try {
                int val = Integer.parseInt(input);
                if (val >= min && val <= max) return val;
            } catch (NumberFormatException ignored) {}
            error("Please enter a number between " + min + " and " + max);
        }
    }

    public static void pressEnter() {
        System.out.print("\n  Press Enter to continue...");
        sc.nextLine();
    }

    public static void blank() { System.out.println(); }
}
