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
public class StartAction implements AbstractAction{

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ErrorService errorService;

    @Autowired
    public StartAction(MachineRepository machineRepository, UserRepository userRepository, SimpMessagingTemplate simpMessagingTemplate, ErrorService errorService) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.errorService = errorService;
    }

    @Override
    public void doMachineAction(Long machineId, Long userId, Boolean isScheduled) {
        isAnotherActionOnMachineRunning(machineId,userId);
        User user = userRepository.findByUserId(userId);
        Machine machine = null;
        try {
            machine = machineRepository.findWithLockingByIdAndCreatedByAndActive(machineId,user, true);
        } catch (ObjectOptimisticLockingFailureException e) {
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "startovano u vreme druge akcije i nije uspelo izvrsavanje"));
            if(isScheduled){
                errorService.addError("startovano u vreme druge akcije i nije uspelo izvrsavanje",machineId,userId,ActionEnum.START);
            }
            return;
        }
        if(machine == null){
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina nije aktivna i ne moze biti startovana"));
            if(isScheduled){
                errorService.addError("masina nije aktivna i ne moze biti startovana",machineId,userId,ActionEnum.START);
            }
            return;
        }
        machine = entityManager.merge(machine);
        if(!machine.getStatus().equalsIgnoreCase("STOPPED")){
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina ne moze biti startovana zato sto nije u stanju stopped"));
            if(isScheduled){
                errorService.addError("masina ne moze biti startovana zato sto nije u stanju stopped",machineId,userId,ActionEnum.START);
            }
            return;
        }
        try {
            sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        machine.setStatus("RUNNING");
        System.out.print("MachineActionStart finished " + user.getFirstName() + " masina "+ machine.getName());
        this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message(user.getFirstName(), "masina " +  machine.getName() + " startovana"));
        this.machineRepository.saveAndFlush(machine);
    }

    private void isAnotherActionOnMachineRunning(Long machineId, Long userId){
        User user = userRepository.findByUserId(userId);
        Machine machine =  machineRepository.findByIdAndCreatedByAndActive(machineId,user, true);
        if(machine == null){
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina nije aktivna i ne moze biti startovana"));
            return;
        }

        try {
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "krenulo"));
            machine.setStatus(machine.getStatus());
            this.machineRepository.save(machine);

        }catch (ObjectOptimisticLockingFailureException exception) {
            System.out.println(exception + "  u startu");
        }
    }

    // za optimistic
//    @Override
//    public Machine doMachineAction(Long machineId, Long userId) {
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
//            machine.setStatus("RUNNING");
//            this.machineRepository.save(machine);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }catch (ObjectOptimisticLockingFailureException exception) {
//            System.out.println(exception + "  u startu");
//        }
//
//        System.out.print("MachineActionStart finished " + user.getFirstName() + " masina "+ machine.getName());
//        this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message(user.getFirstName(), "masina " +  machine.getName() + " startovana"));
//        return machine;
//    }

//pesimistic sa entityManagerom
//    @Override
//    public Machine doMachineAction(Long machineId, Long userId) {
//        User user = userRepository.findByUserId(userId);
//        Machine machine = null;
//
//
//        machine = entityManager.find(Machine.class, machineId);
//        entityManager.lock(machine, LockModeType.PESSIMISTIC_WRITE);
//
//        if(machine == null) return null; // todo masina je izbrisana
//        machine = entityManager.merge(machine);
//        System.out.println("usao");
//        if(!machine.getStatus().equalsIgnoreCase("STOPPED")){
//            // todo baca gresku zato sto ne moze biti startovana
//            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina ne moze biti startovana"));
//            return machine;
//        }
//        try {
//            sleep(1000 * 5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        machine.setStatus("RUNNING");
//        System.out.print("MachineAction finished " + user.getFirstName() + " masina "+ machine.getName());
//        this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message(user.getFirstName(), "masina " +  machine.getName() + " startovana"));
//        return this.machineRepository.saveAndFlush(machine);
//    }
}
