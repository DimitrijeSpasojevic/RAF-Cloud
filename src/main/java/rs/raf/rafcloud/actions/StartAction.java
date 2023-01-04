package rs.raf.rafcloud.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
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
public class StartAction implements AbstractAction{

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public StartAction(MachineRepository machineRepository, UserRepository userRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public Machine doMachineAction(Long machineId, Long userId) {
        User user = userRepository.findByUserId(userId);
        Machine machine = null;
        try {
            machine = machineRepository.findWithLockingByIdAndCreatedByAndActive(machineId,user, true);
        } catch (PessimisticLockingFailureException e) {
            System.out.println("SSIIIIIIIII");
        }
        if(machine == null) return null; // todo masina je izbrisana
        machine = entityManager.merge(machine);
        System.out.println("usao");
        if(!machine.getStatus().equalsIgnoreCase("STOPPED")){
            // todo baca gresku zato sto ne moze biti startovana
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina ne moze biti startovana"));
            return machine;
        }
        try {
            sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        machine.setStatus("RUNNING");
        System.out.print("MachineAction finished " + user.getFirstName() + " masina "+ machine.getName());
        this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message(user.getFirstName(), "masina " +  machine.getName() + " startovana"));
        return this.machineRepository.saveAndFlush(machine);
    }
}
