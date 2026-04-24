package com.testing.demo.repository;

import com.testing.demo.model.User;
import java.util.*;

public class UserRepository {
    private final Map<Long, User> userStore = new HashMap<>();
    private Long currentId = 1L;

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(currentId++);
        }
        userStore.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userStore.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    public void deleteById(Long id) {
        userStore.remove(id);
    }

    public boolean existsById(Long id) {
        return userStore.containsKey(id);
    }

    public void clear() {
        userStore.clear();
        currentId = 1L;
    }
}
