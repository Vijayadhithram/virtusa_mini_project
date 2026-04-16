package library.service;

import library.model.User;
import java.util.*;
import java.util.stream.Collectors;

public class UserService {
    private Map<String, User> users = new LinkedHashMap<>();
    private int idCounter = 1;

    public UserService() {
        // Sample users
        registerUser("Arjun Mehta",  "arjun@email.com",  "9876543210");
        registerUser("Priya Sharma", "priya@email.com",  "9123456780");
        registerUser("Rahul Das",    "rahul@email.com",  "9988776655");
    }

    public User registerUser(String name, String email, String phone) {
        // Prevent duplicate emails
        boolean emailExists = users.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
        if (emailExists) return null;

        String id = String.format("U%03d", idCounter++);
        User user = new User(id, name, email, phone);
        users.put(id, user);
        return user;
    }

    public boolean removeUser(String userId) {
        User u = users.get(userId);
        if (u == null) return false;
        if (u.getBooksIssued() > 0) return false; // has books out
        users.remove(userId);
        return true;
    }

    public boolean updateUser(String userId, String name, String email, String phone) {
        User u = users.get(userId);
        if (u == null) return false;
        if (!name.isBlank())  u.setName(name);
        if (!email.isBlank()) u.setEmail(email);
        if (!phone.isBlank()) u.setPhone(phone);
        return true;
    }

    public User findById(String userId) {
        return users.get(userId);
    }

    public List<User> searchByName(String keyword) {
        String kw = keyword.toLowerCase();
        return users.values().stream()
                .filter(u -> u.getName().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
