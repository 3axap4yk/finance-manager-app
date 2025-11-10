package model;

import java.io.Serializable;

//Категория доходов или расходов. Для расходов можно задать бюджет

public class Category implements Serializable {
    private String name;
    private double budget; // только для расходов

    public Category(String name) {
        this.name = name;
        this.budget = 0;
    }

    public Category(String name, double budget) {
        this.name = name;
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        if (budget < 0) {
            this.budget = 0;
        } else {
            this.budget = budget;
        }
    }

    @Override
    public String toString() {
        if (budget > 0) {
            return name + " (бюджет: " + budget + ")";
        }
        return name;
    }
}
