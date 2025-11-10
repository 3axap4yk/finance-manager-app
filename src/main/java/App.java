import model.User;
import service.AuthService;
import service.StorageService;
import service.WalletService;

import java.util.Scanner;

//Основной класс приложения

public class App {
    public static void main(String[] args) {
        AuthService auth = new AuthService();
        StorageService storage = new StorageService();
        WalletService walletService = new WalletService();
        Scanner sc = new Scanner(System.in);

        User current = null;

        // Меню входа/регистрации
        while (current == null) {
            System.out.println("1. Войти");
            System.out.println("2. Зарегистрироваться");
            System.out.println("0. Выход");
            System.out.print("→ ");
            String cmd = sc.nextLine().trim();

            switch (cmd) {
                case "1" -> {
                    System.out.print("Email: ");
                    String email = sc.nextLine().trim();
                    if (!auth.isValidEmail(email)) {
                        System.out.println("Некорректный email.");
                        break;
                    }
                    User u = storage.loadUser(email);
                    if (u == null) {
                        System.out.println("Пользователь не найден.");
                        break;
                    }
                    System.out.print("Пароль: ");
                    String pass = sc.nextLine();
                    if (!auth.checkPassword(u, pass)) {
                        System.out.println("Неверный пароль.");
                        break;
                    }
                    current = u;
                    System.out.println("Вход выполнен.");
                }
                case "2" -> {
                    System.out.print("Email: ");
                    String email = sc.nextLine().trim();
                    if (!auth.isValidEmail(email)) {
                        System.out.println("Некорректный email.");
                        break;
                    }
                    if (storage.loadUser(email) != null) {
                        System.out.println("Пользователь с таким email уже существует.");
                        break;
                    }
                    System.out.print("Пароль (может быть пустым): ");
                    String pass = sc.nextLine();
                    User newUser = new User(email, auth.hashPassword(pass));

                    // Категории по умолчанию
                    walletService.createIncomeCategory(newUser, "Зарплата");
                    walletService.addIncome(newUser, 25000, "Зарплата");
                    walletService.createExpenseCategory(newUser, "Продукты", 2000);

                    storage.saveUser(newUser);
                    current = newUser;
                    System.out.println("Регистрация завершена.");
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("Неверная команда. Не ломайте пж!");
            }
        }

        // Основное меню
        while (true) {
            System.out.println();
            System.out.println("Текущий пользователь: " + current.getEmail());
            System.out.println("1. Изменить доходы");
            System.out.println("2. Изменить расходы");
            System.out.println("3. Показать статистику");
            System.out.println("4. Управление категориями");
            System.out.println("0. Сохранить и выйти");
            System.out.print("→ ");

            String cmd = sc.nextLine().trim();
            switch (cmd) {
                case "1" -> handleIncomeMenu(walletService, current, sc);
                case "2" -> handleExpenseMenu(walletService, current, sc);
                case "3" -> current.getWallet().printSummary();
                case "4" -> walletService.manageCategories(current, sc); // новое меню категорий
                case "0" -> {
                    storage.saveUser(current);
                    System.out.println("Данные сохранены. До свидания!");
                    return;
                }
                default -> System.out.println("Неверная команда.");
            }
        }
    }

    private static void handleIncomeMenu(WalletService walletService, User user, Scanner sc) {
        while (true) {
            System.out.println("\n1. Добавить категорию дохода");
            System.out.println("2. Добавить доход");
            System.out.println("0. Назад");
            System.out.print("→ ");
            String cmd = sc.nextLine().trim();
            switch (cmd) {
                case "1" -> {
                    System.out.print("Название категории: ");
                    String name = sc.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Название не может быть пустым.");
                        break;
                    }
                    walletService.createIncomeCategory(user, name);
                }
                case "2" -> {
                    if (user.getWallet().getIncomeCategories().isEmpty()) {
                        System.out.println("Сначала добавьте категорию дохода.");
                        break;
                    }
                    System.out.println("Существующие категории:");
                    user.getWallet().getIncomeCategories().keySet()
                            .forEach(cat -> System.out.println("- " + cat));

                    System.out.print("Категория: ");
                    String cat = sc.nextLine().trim();
                    System.out.print("Сумма: ");
                    String sumStr = sc.nextLine().trim();
                    try {
                        double sum = Double.parseDouble(sumStr);
                        walletService.addIncome(user, sum, cat);
                    } catch (NumberFormatException e) {
                        System.out.println("Некорректная сумма.");
                    }
                }
                case "0" -> { return; }
                default -> System.out.println("Неверная команда.");
            }
        }
    }

    private static void handleExpenseMenu(WalletService walletService, User user, Scanner sc) {
        while (true) {
            System.out.println("\n1. Добавить категорию расхода");
            System.out.println("2. Добавить расход");
            System.out.println("0. Назад");
            System.out.print("→ ");
            String cmd = sc.nextLine().trim();
            switch (cmd) {
                case "1" -> {
                    System.out.print("Название категории: ");
                    String name = sc.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Название не может быть пустым.");
                        break;
                    }
                    System.out.print("Бюджет: ");
                    String bStr = sc.nextLine().trim();
                    try {
                        double budget = Double.parseDouble(bStr);
                        walletService.createExpenseCategory(user, name, budget);
                    } catch (NumberFormatException e) {
                        System.out.println("Некорректный ввод бюджета.");
                    }
                }
                case "2" -> {
                    if (user.getWallet().getExpenseCategories().isEmpty()) {
                        System.out.println("Сначала добавьте категорию расхода.");
                        break;
                    }
                    System.out.println("Существующие категории:");
                    user.getWallet().getExpenseCategories().keySet()
                            .forEach(cat -> System.out.println("- " + cat));

                    System.out.print("Категория: ");
                    String cat = sc.nextLine().trim();
                    System.out.print("Сумма: ");
                    String sumStr = sc.nextLine().trim();
                    try {
                        double sum = Double.parseDouble(sumStr);
                        walletService.addExpense(user, sum, cat);
                    } catch (NumberFormatException e) {
                        System.out.println("Некорректная сумма.");
                    }
                }
                case "0" -> { return; }
                default -> System.out.println("Неверная команда.");
            }
        }
    }
}
