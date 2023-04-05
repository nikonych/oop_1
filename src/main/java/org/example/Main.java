package org.example;

import org.example.DAO.UserDAO;
import org.example.enums.Role;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Scanner;

import static org.example.Decor.*;

public class Main {


    public static void main(String[] args) {
        runApp();
    }

    private static void runApp() {
        Scanner scanner = new Scanner(System.in);
        String answer;
        Boolean isRun = true;
        while(isRun) {
            printBlue("1) Войти \n" +
                    "2) Регистрация \n" +
                    "3) Выход :(");
            answer = scanner.next();
            switch (answer){
                case "1": {
                    clearScreen();
                    login(scanner);
                    break;
                }
                case "2": {
                    clearScreen();
                    signup(scanner);
                    break;
                }
                case "3": {
                    isRun = false;
                    break;
                }
            }
        }
    }

    private static void signup(Scanner scanner) {
        Role role = Role.VOTER;
        printBlue("Имя:");
        String name = scanner.next();
        printBlue("Пароль:");
        String password = scanner.next();
        User user = new User(name, password, role);
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            session.close();
            printGreen("Успешно!");
        }catch (Exception e){
            printRed("Такой пользователь уже есть :(");
        }
    }
    private static void login(Scanner scanner) {
        printBlue("Имя:");
        String name = scanner.next();
        printBlue("Пароль:");
        String password = scanner.next();
        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticate(name, password);
        if(user != null){
            printGreen("Успешный вход!");
            mainMenu(scanner, user);
        }else {
            printRed("Неверный пароль");
        }
    }

    private static void mainMenu(Scanner scanner, User user) {
        switch (user.getRole()){
            case VOTER -> {
                voterMenu(scanner, user);
            }
            case CANDIDATE -> {
                candidateMenu(scanner, user);
            }
            case ADMIN -> {
                adminMenu(scanner, user);
            }
        }
    }

    private static void adminMenu(Scanner scanner, User user) {
        Boolean isRun = true;
        while (isRun) {
            printBlue("1) Голосовать\n" +
                    "2) Добавить кандидата\n" +
                    "3) Подсчет голосов\n" +
                    "4) Выход\n");
            String answer = scanner.next();
            switch (answer){
                case "1" -> {
                    if (!user.getHasVoted())
                        voteList(scanner, user);
                    else {
                        printRed("Вы уже проголосовали!");
                    }
                }
                case "2" -> {
                    addCandidate(scanner);
                }
                case "3" -> {
                    UserDAO userDAO = new UserDAO();
                    List<User> users = userDAO.getCandidates();
                    printResult(users);
                }
                case "4" -> {
                    isRun = false;
                }
            }
        }

    }

    private static void addCandidate(Scanner scanner) {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getVoters();
        if (!users.isEmpty()) {
            printCandidates(users);
            printGreen("Выберите номер пользователя от 0 .. " + (users.size()-1));
            String answer = scanner.next();
            try {
                if(Integer.parseInt(answer) >= 0 && Integer.parseInt(answer) < users.size()){
                    User candidate = users.get((Integer.parseInt(answer)));
                    candidate.setRole(Role.CANDIDATE);
                    userDAO.updateUser(candidate);
                    printGreen("Успешно!");
                }
            } catch (Exception e){
                printRed("Ошибка");
            }
        } else {
            printRed("Нету Пользователя на роль кандидата");
        }
    }

    private static void candidateMenu(Scanner scanner, User user) {
        Boolean isRun = true;
        while (isRun) {
            printBlue("1) Голосовать\n" +
                    "2) Количество голосов\n" +
                    "3) Выход\n");
            String answer = scanner.next();
            switch (answer) {
                case "1" -> {
                    if (!user.getHasVoted())
                        voteList(scanner, user);
                    else {
                        printRed("Вы уже проголосовали!");
                    }
                }
                case "2" -> {
                    printGreen("Количество голосов: " + user.getVoteCount());
                }
                case "3" -> {
                    isRun = false;
                }
            }
        }

    }

    private static void voterMenu(Scanner scanner, User user) {
        Boolean isRun = true;
        while (isRun) {
            printBlue("1) Голосовать\n" +
                    "2) Выход\n");
            String answer = scanner.next();
            switch (answer) {
                case "1" -> {
                    if (!user.getHasVoted())
                        voteList(scanner, user);
                    else {
                        printRed("Вы уже проголосовали!");
                    }
                }
                case "2" -> {
                    isRun = false;
                }
            }
        }
    }

    private static void voteList(Scanner scanner, User user) {
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getCandidates();
        if (!users.isEmpty()) {
            printCandidates(users);
            printGreen("Выберите номер кандидата от 0 .. " + (users.size()-1));
            String answer = scanner.next();
            try {
                if(Integer.parseInt(answer) >= 0 && Integer.parseInt(answer) < users.size()){
                    user.setHasVoted(true);
                    User candidate = users.get((Integer.parseInt(answer)));
                    candidate.setVoteCount(candidate.getVoteCount()+1);
                    userDAO.updateUser(user);
                    userDAO.updateUser(candidate);
                    printGreen("Успешно!");
                }
            } catch (Exception e){
                printRed("Ошибка");
            }
        } else {
            printRed("Кандидатов нету");
        }
    }

}