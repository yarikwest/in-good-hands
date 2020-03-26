package pl.coderslab.charity.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.exceptions.EmailExistsException;
import pl.coderslab.charity.exceptions.RoleNotFoundException;
import pl.coderslab.charity.exceptions.UserNotFoundException;
import pl.coderslab.charity.model.Role;
import pl.coderslab.charity.model.User;
import pl.coderslab.charity.model.VerificationToken;
import pl.coderslab.charity.repository.VerificationTokenRepository;
import pl.coderslab.charity.repository.RoleRepository;
import pl.coderslab.charity.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    UserService(UserRepository userRepository, RoleRepository roleRepository,
                BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public User registerNewUser(User user) throws EmailExistsException {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailExistsException();
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(false);
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(RoleNotFoundException::new);
        user.getRoles().add(userRole);
        return userRepository.save(user);
    }

    public void activateUser(User user) {
        user.setActive(true);
        userRepository.save(user);
    }

    public Set<User> getAllUsers() {
        return userRepository.findAllByRole(roleRepository.findByName("ROLE_USER").orElseThrow(RoleNotFoundException::new));
    }

    public Set<User> getAllAdmins() {
        return userRepository.findAllByRole(roleRepository.findByName("ROLE_ADMIN").orElseThrow(RoleNotFoundException::new));
    }

    public User getById(long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.warn("IN getById(id): user with id {} not founded", id);
            return new UserNotFoundException(id);
        });
    }

    public User updateUserData(long id, User user) {

        User oldUser = userRepository.findById(id).orElseThrow(() -> {
            log.warn("IN update(): user with id {} not exists", id);
            throw new UserNotFoundException(id);
        });

        oldUser.setEmail(user.getEmail());
        oldUser.setActive(user.getActive());
        return userRepository.save(oldUser);
    }

    public User updateAdminData(long id, User user) {

        User oldUser = userRepository.findById(id).orElseThrow(() -> {
            log.warn("IN updateAdminData(): user with id {} not exists", id);
            throw new UserNotFoundException(id);
        });

        oldUser.setEmail(user.getEmail());
        return userRepository.save(oldUser);
    }

    public User createAdmin(User user) {
        user.setPassword(bCryptPasswordEncoder.encode("admin"));
        user.setActive(true);
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElseThrow(RoleNotFoundException::new);
        user.getRoles().add(roleAdmin);
        return userRepository.save(user);
    }

    public void delete(long id) {
        if (!userRepository.existsById(id)) {
            log.warn("IN delete(): user with id {} not exists", id);
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public void toggleActive(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("IN block(): user with id {} not exists", id);
            throw new UserNotFoundException(id);
        });

        user.setActive(!user.getActive());
        userRepository.save(user);
    }

    public long getCountAll() {
        return userRepository.countAllByRole(roleRepository.findByName("ROLE_USER").orElseThrow(RoleNotFoundException::new));
    }

    public long getCountAllNewUserFromLastMonth() {
        LocalDateTime period = LocalDateTime.now().minusMonths(1);
        return userRepository.countAllByRoleAndCreatedFromLastMonth(
                roleRepository.findByName("ROLE_USER").orElseThrow(RoleNotFoundException::new),
                period
        );
    }

    public boolean checkIfValidOldPassword(User loggedInUser, String oldPassword) {
        return bCryptPasswordEncoder.matches(oldPassword, loggedInUser.getPassword());
    }

    public boolean checkIfEmailAlreadyExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public void changeUserPassword(User loggedInUser, String password) {
        loggedInUser.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(loggedInUser);
    }

    public void changeUserEmail(User loggedInUser, String email) {
        loggedInUser.setEmail(email);
        userRepository.save(loggedInUser);
    }
}
