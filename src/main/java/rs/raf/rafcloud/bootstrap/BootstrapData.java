package rs.raf.rafcloud.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.rafcloud.model.Role;
import rs.raf.rafcloud.model.User;
import rs.raf.rafcloud.repositories.RoleRepository;
import rs.raf.rafcloud.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;


@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

        String[] ROLES = {"can_create_users", "can_read_users", "can_update_users", "can_delete_users"};

        List<Role> roles = new ArrayList<>();

        for (int i = 0; i < ROLES.length; i++) {
            Role role = new Role();
            role.setRole(ROLES[i]);
            roles.add(role);
        }
        roleRepository.saveAll(roles);



        User user1 = new User();
        user1.setUsername("pera@gmail.com");
        user1.setFirstName("Pera");
        user1.setLastName("Peric");
        user1.setPassword(this.passwordEncoder.encode("123123123"));
        user1.addRole(roles.get(0));
        user1.addRole(roles.get(1));
        user1.addRole(roles.get(2));
        user1.addRole(roles.get(3));

        this.userRepository.save(user1);

        System.out.println("Data loaded!");
    }
}
