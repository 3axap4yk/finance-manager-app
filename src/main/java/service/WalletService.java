package service;

import model.User;
import model.Wallet;
import model.Category;

import java.util.Scanner;

//Сервис для управления кошельком пользователя
public class WalletService {

    // Создать категорию дохода
    public void createIncomeCategory(User user, String name) {
        Wallet wallet = user.getWallet();
        if (wallet.getIncomeCategories().containsKey(name)) {
            System.out.println("Категория дохода уже существует.");
            return;
        }
        wallet.addIncomeCategory(name);
        System.out.println("Категория дохода \"" + name + "\" добавлена.");
    }

    // Создать категорию расхода
    public void createExpenseCategory(User user, String name, double budget) {
        Wallet wallet = user.getWallet();
        if (wallet.getExpenseCategories().containsKey(name)) {
            System.out.println("Категория расхода уже существует.");
            return;
        }
        wallet.addExpenseCategory(name, budget);
        System.out.println("Категория расхода \"" + name + "\" с бюджетом " + budget + " добавлена.");
    }

    // Добавить доход
    public void addIncome(User user, double amount, String category) {
        Wallet wallet = user.getWallet();
        if (amount <= 0) {
            System.out.println("Сумма должна быть положительной.");
            return;
        }
        if (!wallet.getIncomeCategories().containsKey(category)) {
            System.out.println("Категория дохода не найдена.");
            return;
        }
        wallet.addIncome(category, amount, "");
        System.out.println("Доход добавлен: " + amount + " (" + category + ")");
    }

    // Добавить расход
    public void addExpense(User user, double amount, String category) {
        Wallet wallet = user.getWallet();
        if (amount <= 0) {
            System.out.println("Сумма должна быть положительной.");
            return;
        }
        if (!wallet.getExpenseCategories().containsKey(category)) {
            System.out.println("Категория расхода не найдена.");
            return;
        }
        wallet.addExpense(category, amount, "");
        System.out.println("Расход добавлен: " + amount + " (" + category + ")");
    }

    // Показать все категории с суммами
    public void showCategories(User user) {
        Wallet wallet = user.getWallet();

        System.out.println("\nДоходы:");
        for (Category c : wallet.getIncomeCategories().values()) {
            double sum = wallet.getIncomes().stream()
                    .filter(i -> i.getCategory().equals(c.getName()))
                    .mapToDouble(i -> i.getAmount())
                    .sum();
            System.out.println("- " + c.getName() + " (сумма: " + sum + ")");
        }

        System.out.println("\nРасходы:");
        for (Category c : wallet.getExpenseCategories().values()) {
            double spent = wallet.getTotalExpenseByCategory(c.getName());
            System.out.println("- " + c.getName() + " (бюджет: " + c.getBudget() + ", потрачено: " + spent + ")");
        }
    }

    // Редактировать категорию дохода
    public void editIncomeCategory(User user, String oldName, String newName) {
        Wallet wallet = user.getWallet();
        if (!wallet.getIncomeCategories().containsKey(oldName)) {
            System.out.println("Категория дохода не найдена.");
            return;
        }
        if (wallet.getIncomeCategories().containsKey(newName)) {
            System.out.println("Категория с таким названием уже существует.");
            return;
        }
        Category cat = wallet.getIncomeCategories().remove(oldName);
        cat = new Category(newName); // создаём новую с новым именем, бюджет не нужен
        wallet.getIncomeCategories().put(newName, cat);
        System.out.println("Категория дохода переименована: " + newName);
    }

    // Редактировать категорию расхода
    public void editExpenseCategory(User user, String oldName, String newName, double newBudget) {
        Wallet wallet = user.getWallet();
        if (!wallet.getExpenseCategories().containsKey(oldName)) {
            System.out.println("Категория расхода не найдена.");
            return;
        }
        if (!oldName.equals(newName) && wallet.getExpenseCategories().containsKey(newName)) {
            System.out.println("Категория с таким названием уже существует.");
            return;
        }
        Category cat = wallet.getExpenseCategories().remove(oldName);
        cat = new Category(newName, newBudget);
        wallet.getExpenseCategories().put(newName, cat);
        System.out.println("Категория расхода обновлена: " + newName + ", бюджет: " + newBudget);
    }

    // Удалить категорию дохода
    public void deleteIncomeCategory(User user, String name) {
        Wallet wallet = user.getWallet();
        if (!wallet.getIncomeCategories().containsKey(name)) {
            System.out.println("Категория дохода не найдена.");
            return;
        }
        wallet.getIncomeCategories().remove(name);
        wallet.getIncomes().removeIf(i -> i.getCategory().equals(name));
        System.out.println("Категория дохода \"" + name + "\" удалена.");
    }

    // Удалить категорию расхода
    public void deleteExpenseCategory(User user, String name) {
        Wallet wallet = user.getWallet();
        if (!wallet.getExpenseCategories().containsKey(name)) {
            System.out.println("Категория расхода не найдена.");
            return;
        }
        wallet.getExpenseCategories().remove(name);
        wallet.getExpenses().removeIf(e -> e.getCategory().equals(name));
        System.out.println("Категория расхода \"" + name + "\" удалена.");
    }

    // Показать категории и дать выбор редактирования/удаления
    public void manageCategories(User user, Scanner sc) {
        while (true) {
            System.out.println("\n1. Переименовать категории дохода");
            System.out.println("2. Переименовать/изменить бюджет категории расхода");
            System.out.println("3. Удалить категорию дохода");
            System.out.println("4. Удалить категорию расхода");
            System.out.println("5. Показать категории");
            System.out.println("0. Назад");
            System.out.print("→ ");
            String cmd = sc.nextLine().trim();
            switch (cmd) {
                case "1" -> {
                    System.out.print("Старая категория дохода: ");
                    String oldName = sc.nextLine().trim();
                    System.out.print("Новое название: ");
                    String newName = sc.nextLine().trim();
                    editIncomeCategory(user, oldName, newName);
                }
                case "2" -> {
                    System.out.print("Старая категория расхода: ");
                    String oldName = sc.nextLine().trim();
                    System.out.print("Новое название: ");
                    String newName = sc.nextLine().trim();
                    System.out.print("Новый бюджет: ");
                    String bStr = sc.nextLine().trim();
                    try {
                        double budget = Double.parseDouble(bStr);
                        editExpenseCategory(user, oldName, newName, budget);
                    } catch (NumberFormatException e) {
                        System.out.println("Некорректный ввод бюджета.");
                    }
                }
                case "3" -> {
                    System.out.print("Категория дохода для удаления: ");
                    String name = sc.nextLine().trim();
                    deleteIncomeCategory(user, name);
                }
                case "4" -> {
                    System.out.print("Категория расхода для удаления: ");
                    String name = sc.nextLine().trim();
                    deleteExpenseCategory(user, name);
                }
                case "5" -> showCategories(user);
                case "0" -> { return; }
                default -> System.out.println("Неверная команда.");
            }
        }
    }
}
