package rs.raf.rafcloud.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.raf.rafcloud.model.Machine;
import rs.raf.rafcloud.model.Role;
import rs.raf.rafcloud.model.User;
import rs.raf.rafcloud.repositories.MachineRepository;
import rs.raf.rafcloud.repositories.RoleRepository;
import rs.raf.rafcloud.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;


@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MachineRepository machineRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(UserRepository userRepository, RoleRepository roleRepository, MachineRepository machineRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.machineRepository = machineRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading Data...");

        String[] ROLES = {"can_create_users", "can_read_users", "can_update_users", "can_delete_users",
                "can_search_machines", "can_start_machines", "can_stop_machines", "can_restart_machines", "can_create_machines", "can_destroy_machines"};

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
        user1.addRole(roles.get(4));
        user1.addRole(roles.get(5));
        user1.addRole(roles.get(6));
        user1.addRole(roles.get(7));
        user1.addRole(roles.get(8));
        user1.addRole(roles.get(9));


        this.userRepository.save(user1);

        Machine machine = new Machine();
        machine.setActive(true);
        machine.setCreatedBy(user1);
        machine.setStatus("STOPPED");
        machineRepository.save(machine);

        System.out.println("Data loaded!");
    }
}
