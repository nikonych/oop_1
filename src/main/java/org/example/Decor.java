package org.example;

import org.example.model.User;

import java.util.List;

public class Decor {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    static void printBlue(String text){
        System.out.println(ANSI_BLUE + text + ANSI_RESET);
    }

    static void printGreen(String text){
        System.out.println(ANSI_GREEN + text + ANSI_RESET);
    }

    static void printRed(String text){
        System.out.println(ANSI_RED + text + ANSI_RESET);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printCandidates(List<User> users){
        for (int i = 0; i < users.size(); i++) {
            System.out.printf(ANSI_GREEN + "%2s %4s\n" + ANSI_RESET, i, users.get(i).getName());
        }
    }

    public static void printResult(List<User> users){
        printGreen("Победитель: " + users.get(0).getName());
        printGreen("Количество голосов: " + users.get(0).getVoteCount());

        for (int i = 1; i < users.size(); i++) {
            printBlue("Кандидат: " + users.get(i).getName());
            printBlue("Количество голосов: " + users.get(i).getVoteCount());
        }
        printBlue(" ");
    }
}
