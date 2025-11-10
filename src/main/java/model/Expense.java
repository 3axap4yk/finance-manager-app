package model;

//Класс расхода (наследник Transaction)
public class Expense extends Transaction {
    public Expense(String category, double amount, String description) {
        super(category, amount, description);
    }

    @Override
    public String getType() {
        return "Расход";
    }
}
