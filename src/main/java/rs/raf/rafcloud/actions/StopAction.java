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
public class StopAction implements AbstractAction{

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public StopAction(MachineRepository machineRepository, UserRepository userRepository, SimpMessagingTemplate simpMessagingTemplate) {
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
            // todo baca gresku zato sto ne moze biti stop
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina ne moze biti stopirana zato sto nije u stanju running"));
            return;
        }
        try {
            sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        machine.setStatus("STOPPED");
        System.out.print("MachineAction finished");
        this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina je stopirana"));
        this.machineRepository.saveAndFlush(machine);
    }


//    @Override
//    public Machine doMachineAction(Long machineId, Long userId) { // sa optimistic lockom
//        User user = userRepository.findByUserId(userId);
//        Machine machine =  machineRepository.findByIdAndCreatedByAndActive(machineId,user, true);
////        if(machine == null) return null; // todo masina je izbrisana
////        if(!machine.getStatus().equalsIgnoreCase("STOPPED")){
////            // todo baca gresku zato sto ne moze biti startovana
////            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina ne moze biti startovana"));
////            return machine;
////        }
//        try {
//            sleep(1000 * 5);
//
//            machine.setStatus("STOPPED");
//            this.machineRepository.save(machine);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }catch (ObjectOptimisticLockingFailureException exception) {
//            System.out.println(exception + " u stopu");
//        }
//
//        System.out.print("MachineActionStop finished " + user.getFirstName() + " masina "+ machine.getName());
//        this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message(user.getFirstName(), "masina " +  machine.getName() + " stopirana"));
//        return machine;
//    }
}
