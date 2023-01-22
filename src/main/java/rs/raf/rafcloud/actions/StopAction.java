package rs.raf.rafcloud.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import rs.raf.rafcloud.model.Machine;
import rs.raf.rafcloud.model.Message;
import rs.raf.rafcloud.model.User;
import rs.raf.rafcloud.repositories.MachineRepository;
import rs.raf.rafcloud.repositories.UserRepository;
import rs.raf.rafcloud.services.ErrorService;

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
    private final ErrorService errorService;


    @Autowired
    public StopAction(MachineRepository machineRepository, UserRepository userRepository, SimpMessagingTemplate simpMessagingTemplate, ErrorService errorService) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.errorService = errorService;
    }

    @Override
    public void doMachineAction(Long machineId, Long userId, Boolean isScheduled) {
        User user = userRepository.findByUserId(userId);
        isAnotherActionOnMachineRunning(machineId,userId,user);
        Machine machine = null;
        try {
            machine = machineRepository.findWithLockingByIdAndCreatedByAndActive(machineId,user, true);
        } catch (ObjectOptimisticLockingFailureException e) {
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "stop u vreme druge akcije i nije uspelo izvrsavanje"));
            if(isScheduled){
                errorService.addError("stop u vreme druge akcije i nije uspelo izvrsavanje",machineId,userId,ActionEnum.STOP);
            }
            return;
        }
        if(machine == null){
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina nije aktivna i ne moze biti stopirana"));
            if(isScheduled){
                errorService.addError("masina nije aktivna i ne moze biti stopirana",machineId,userId,ActionEnum.STOP);
            }
            return;
        }
        machine = entityManager.merge(machine);
        if(!machine.getStatus().equalsIgnoreCase("RUNNING")){
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina ne moze biti stopirana zato sto nije u stanju running"));
            if(isScheduled){
                errorService.addError("masina ne moze biti stopirana zato sto nije u stanju running",machineId,userId,ActionEnum.STOP);
            }
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
    private void isAnotherActionOnMachineRunning(Long machineId, Long userId, User user){
        Machine machine =  machineRepository.findByIdAndCreatedByAndActive(machineId,user, true);
        if(machine == null){
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina nije aktivna i ne moze biti stopirana"));
            return;
        }
        try {
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "krenulo"));
            machine.setStatus(machine.getStatus());
            this.machineRepository.save(machine);

        }catch (ObjectOptimisticLockingFailureException exception) {
            System.out.println(exception + "  u stopu");
        }
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
