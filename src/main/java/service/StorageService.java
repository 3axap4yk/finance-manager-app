package service;

import com.google.gson.*;
import model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;

// Сервис для сохранения и загрузки пользователей. Каждый пользователь хранится в отдельном JSON-файле (email.json)

public class StorageService {
    private static final String DATA_DIR = "data";
    private final Gson gson;

    public StorageService() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }

        // Регистрируем для LocalDate
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                    @Override
                    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(date.toString());
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        return LocalDate.parse(json.getAsString());
                    }
                })
                .setPrettyPrinting()
                .create();
    }

    // Сохранить пользователя в JSON
    public void saveUser(User user) {
        String fileName = DATA_DIR + "/" + sanitizeFileName(user.getEmail()) + ".json";
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(user, writer);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    // Загрузить пользователя по email
    public User loadUser(String email) {
        String fileName = DATA_DIR + "/" + sanitizeFileName(email) + ".json";
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, User.class);
        } catch (IOException e) {
            System.out.println("Пу-пу-пу... Ошибка при загрузке данных: " + e.getMessage());
            return null;
        }
    }

    // Заменяем недопустимые символы для имени файла
    private String sanitizeFileName(String email) {
        return email.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
