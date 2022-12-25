package rs.raf.rafcloud.mappers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.rafcloud.model.Role;
import rs.raf.rafcloud.model.User;
import rs.raf.rafcloud.repositories.RoleRepository;
import rs.raf.rafcloud.repositories.UserRepository;
import rs.raf.rafcloud.requests.UpdateUserRequest;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateUserReqToUser {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    UpdateUserReqToUser(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User mapReqToUser(UpdateUserRequest request){
        User user = userRepository.findByUserId(request.getUserId());
        List<Role> userRoles = new ArrayList<>();
        userRoles.addAll(user.getRoles());
        for (Role r: userRoles){
            user.removeRole(r);
        }
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setUsername(request.getUsername());

        for (int i = 0; i < request.getRoles().size(); i++) {
            Role role = roleRepository.findByRole(request.getRoles().get(i));
            user.addRole(role);
        }
        return user;
    }
}
