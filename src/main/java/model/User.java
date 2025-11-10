package model;

import java.io.Serializable;

//Класс пользователя. Каждый пользователь имеет email, пароль (в виде хеша) и кошелйок

public class User implements Serializable {
    private String email;
    private String passwordHash;
    private Wallet wallet;

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.wallet = new Wallet();
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
