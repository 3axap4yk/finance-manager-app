package model;

// Класс дохода (наследник Transaction)
public class Income extends Transaction {
    public Income(String category, double amount, String description) {
        super(category, amount, description);
    }

    @Override
    public String getType() {
        return "Доход";
    }
}
