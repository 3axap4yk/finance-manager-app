package model;

import java.io.Serializable;
import java.time.LocalDate;

// Абстрактный класс для операций (доходы и расходы)

public abstract class Transaction implements Serializable {
    protected String category;
    protected double amount;
    protected LocalDate date;
    protected String description;

    public Transaction(String category, double amount, String description) {
        this.category = category;
        this.amount = amount;
        this.description = description;
        this.date = LocalDate.now();
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public abstract String getType(); // доход или расходд

    @Override
    public String toString() {
        return "[" + getType() + "] " + category + ": " + amount + " (" + date + ")";
    }
}
