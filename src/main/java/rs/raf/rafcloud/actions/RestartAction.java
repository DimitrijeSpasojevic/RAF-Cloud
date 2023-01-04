package rs.raf.rafcloud.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import rs.raf.rafcloud.model.Machine;
import rs.raf.rafcloud.model.Message;
import rs.raf.rafcloud.model.User;
import rs.raf.rafcloud.repositories.MachineRepository;
import rs.raf.rafcloud.repositories.UserRepository;

import javax.persistence.EntityManager;

import static java.lang.Thread.sleep;

@Component
public class RestartAction implements AbstractAction{

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public RestartAction(MachineRepository machineRepository, UserRepository userRepository, EntityManager entityManager, SimpMessagingTemplate simpMessagingTemplate) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public Machine doMachineAction(Long machineId, Long userId) {
        User user = userRepository.findByUserId(userId);
        Machine machine = machineRepository.findWithLockingByIdAndCreatedByAndActive(machineId,user, true);
        if(machine == null) return null; // todo masina je izbrisana
        machine = entityManager.merge(machine);
        if(!machine.getStatus().equalsIgnoreCase("RUNNING")){
            // todo baca gresku zato sto ne moze biti restartovana
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina ne moze biti restartovana"));
            return machine;
        }
        try {
            sleep(1000 * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        machine.setStatus("STOPPED");
        this.machineRepository.saveAndFlush(machine);
        this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina u pola faze restartovanja"));
        try {
            sleep(1000 * 4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        machine.setStatus("RUNNING");
        System.out.print("MachineAction finished");
        this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina restartovana"));
        return this.machineRepository.saveAndFlush(machine);
    }
}
