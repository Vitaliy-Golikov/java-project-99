package hexlet.code.app.service;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.UserMapper;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::map)
                .toList();
    }

    public UserDTO getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return userMapper.map(user);
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        var user = userMapper.map(userCreateDTO);
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPasswordDigest()));
        userRepository.save(user);
        return userMapper.map(user);
    }

    public UserDTO updateUser(UserUpdateDTO userUpdateDTO, Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));

        // ✅ Правильная работа с JsonNullable
        if (userUpdateDTO.getFirstName() != null && userUpdateDTO.getFirstName().isPresent()) {
            user.setFirstName(userUpdateDTO.getFirstName().get());
        }
        if (userUpdateDTO.getLastName() != null && userUpdateDTO.getLastName().isPresent()) {
            user.setLastName(userUpdateDTO.getLastName().get());
        }
        if (userUpdateDTO.getEmail() != null && userUpdateDTO.getEmail().isPresent()) {
            user.setEmail(userUpdateDTO.getEmail().get());
        }
        if (userUpdateDTO.getPassword() != null && userUpdateDTO.getPassword().isPresent()) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword().get()));
        }

        userRepository.save(user);
        return userMapper.map(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }
}