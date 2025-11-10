package service;

import model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

// Сервис авторизации и проверки данных пользователя

public class AuthService {

    // Проверка, что email в корректном формате
    public boolean isValidEmail(String email) {
        String regex = "^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }

    // хеширование пароля (SHA-256)
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Если что-то не так с алгоритмом
            return Integer.toString(password.hashCode());
        }
    }

    // Проверка пароля
    public boolean checkPassword(User user, String password) {
        return user.getPasswordHash().equals(hashPassword(password));
    }
}
