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
import javax.persistence.PersistenceContext;

import static java.lang.Thread.sleep;

@Component
public class RestartAction implements AbstractAction{

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public RestartAction(MachineRepository machineRepository, UserRepository userRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void doMachineAction(Long machineId, Long userId) {
        User user = userRepository.findByUserId(userId);
        Machine machine = machineRepository.findWithLockingByIdAndCreatedByAndActive(machineId,user, true);
        if(machine == null) return; // todo masina je izbrisana
        machine = entityManager.merge(machine);
        if(!machine.getStatus().equalsIgnoreCase("RUNNING")){
            // todo baca gresku zato sto ne moze biti restartovana
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina ne moze biti restartovana"));
            return;
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
        this.machineRepository.saveAndFlush(machine);
    }
}
