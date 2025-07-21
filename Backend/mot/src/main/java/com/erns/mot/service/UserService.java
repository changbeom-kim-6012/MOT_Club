package com.erns.mot.service;

import com.erns.mot.domain.User;
import com.erns.mot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없거나 비밀번호가 틀렸습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("사용자를 찾을 수 없거나 비밀번호가 틀렸습니다.");
        }
        return user;
    }

    public User updateRemarks(Long id, String remarks) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setRemarks(remarks);
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setActivityLevel(userDetails.getActivityLevel());
        // 비밀번호는 별도의 API에서 변경하도록 하는 것이 일반적입니다. 여기서는 수정하지 않습니다.
        return userRepository.save(user);
    }

    // 비밀번호 재설정(변경)
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 사용자를 찾을 수 없습니다."));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // 회원 일괄 등록
    public List<User> bulkCreateUsers(List<User> users) {
        for (User user : users) {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new IllegalArgumentException("이미 사용중인 이메일이 있습니다: " + user.getEmail());
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.saveAll(users);
    }

    // 회원 삭제
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("해당 사용자를 찾을 수 없습니다.");
        }
        userRepository.deleteById(id);
    }

    // 비밀번호 변경
    public void changePassword(Long id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
} 