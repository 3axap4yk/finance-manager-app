package model;

import java.io.Serializable;
import java.util.*;

// Кошелек пользователя. Содержит доходы, расходы и категории с бюджетами
public class Wallet implements Serializable {
    private List<Income> incomes;
    private List<Expense> expenses;
    private Map<String, Category> incomeCategories;
    private Map<String, Category> expenseCategories;

    public Wallet() {
        incomes = new ArrayList<>();
        expenses = new ArrayList<>();
        incomeCategories = new HashMap<>();
        expenseCategories = new HashMap<>();
    }

    // Добавить категорию дохода
    public void addIncomeCategory(String name) {
        if (!incomeCategories.containsKey(name)) {
            incomeCategories.put(name, new Category(name));
        }
    }

    // Добавить категорию расхода с бюджетом
    public void addExpenseCategory(String name, double budget) {
        if (!expenseCategories.containsKey(name)) {
            expenseCategories.put(name, new Category(name, budget));
        }
    }

    // Добавить доход
    public void addIncome(String category, double amount, String desc) {
        if (incomeCategories.containsKey(category)) {
            incomes.add(new Income(category, amount, desc));
        } else {
            System.out.println("Категория дохода не найдена: " + category);
        }
    }

    // Добавить расход
    public void addExpense(String category, double amount, String desc) {
        if (expenseCategories.containsKey(category)) {
            expenses.add(new Expense(category, amount, desc));
            checkBudgetLimit(category);
        } else {
            System.out.println("Категория расхода не найдена: " + category);
        }
    }

    // Проверить, превышен ли бюджет по категории
    private void checkBudgetLimit(String category) {
        double total = getTotalExpenseByCategory(category);
        double budget = expenseCategories.get(category).getBudget();
        if (budget > 0) {
            if (total > budget) {
                System.out.println("Внимание: превышен бюджет по категории \"" + category + "\"!");
            } else if (total > budget * 0.8) {
                System.out.println("Предупреждение: потрачено более 80% бюджета по категории \"" + category + "\".");
            }
        }
    }

    // Подсчёт общей суммы доходов
    public double getTotalIncome() {
        return incomes.stream().mapToDouble(Income::getAmount).sum();
    }

    // Подсчёт общей суммы расходов
    public double getTotalExpense() {
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    // Подсчёт расходов по категории
    public double getTotalExpenseByCategory(String category) {
        return expenses.stream()
                .filter(e -> e.getCategory().equals(category))
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    // Вывести текущую статастику
    public void printSummary() {
        System.out.println("Общий доход: " + getTotalIncome());
        System.out.println("Общие расходы: " + getTotalExpense());
        System.out.println();

        System.out.println("Бюджеты по категориям расходов:");
        for (Category cat : expenseCategories.values()) {
            double spent = getTotalExpenseByCategory(cat.getName());
            double remaining = cat.getBudget() - spent;
            System.out.println("- " + cat.getName() + ": бюджет " + cat.getBudget() + ", остаток " + remaining);
        }
    }

    public Map<String, Category> getIncomeCategories() {
        return incomeCategories;
    }

    public Map<String, Category> getExpenseCategories() {
        return expenseCategories;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
}
