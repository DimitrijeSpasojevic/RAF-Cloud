package rs.raf.rafcloud.actions;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.rafcloud.model.Machine;
import rs.raf.rafcloud.model.Message;
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
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    public MyBean(MachineRepository machineRepository, UserRepository userRepository, EntityManager entityManager, SimpMessagingTemplate simpMessagingTemplate, SimpUserRegistry simpUserRegistry) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.simpUserRegistry = simpUserRegistry;
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
        this.simpMessagingTemplate.convertAndSend("/topic/messages/" + machineId, new Message("server", "masina startovana"));
        return this.machineRepository.saveAndFlush(machine);
    }
}
