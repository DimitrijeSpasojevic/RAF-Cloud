package rs.raf.rafcloud.actions;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.rafcloud.model.Machine;
import rs.raf.rafcloud.model.User;
import rs.raf.rafcloud.repositories.MachineRepository;
import rs.raf.rafcloud.repositories.UserRepository;

import javax.persistence.EntityManager;

import static java.lang.Thread.sleep;

@Component
public class MyBean {

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    public MyBean(MachineRepository machineRepository, UserRepository userRepository, EntityManager entityManager) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public Machine startMachine(Long machineId, Long userId){
        System.out.print("StartAction running");
        User user = userRepository.findByUserId(userId);
        Machine machine = machineRepository.findWithLockingByIdAndCreatedBy(machineId,user);
        machine = entityManager.merge(machine);
        System.out.println("------USAOOO--------");
        try {
            sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        machine.setStatus("RUNNING");
        System.out.print("StartAction finished");
        return this.machineRepository.saveAndFlush(machine);
    }
}
