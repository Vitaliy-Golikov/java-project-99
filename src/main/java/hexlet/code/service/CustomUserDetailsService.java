package hexlet.code.service;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDetails user) {
        User newUser = new User();
        newUser.setEmail(user.getUsername());
        newUser.setPasswordDigest(passwordEncoder.encode(user.getPassword()));
        // Если у UserDetails есть другие поля, установите их
        userRepository.save(newUser);
    }

    @Override
    public void updateUser(UserDetails user) {
        // Найти пользователя по email и обновить
        userRepository.findByEmail(user.getUsername())
                .ifPresent(existingUser -> {
                    // Обновить поля
                    userRepository.save(existingUser);
                });
    }

    @Override
    public void deleteUser(String username) {
        userRepository.findByEmail(username)
                .ifPresent(userRepository::delete);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // Реализация смены пароля
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByEmail(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}