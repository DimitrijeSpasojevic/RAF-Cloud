package rs.raf.rafcloud.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
public class RestartAction implements AbstractAction{

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ErrorService errorService;

    @Autowired
    public RestartAction(MachineRepository machineRepository, UserRepository userRepository, SimpMessagingTemplate simpMessagingTemplate, ErrorService errorService) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.errorService = errorService;
    }

    @Override
    public void doMachineAction(Long machineId, Long userId, Boolean isScheduled) {
        entityManager.setProperty("javax.persistence.query.timeout", 1000);
        User user = userRepository.findByUserId(userId);
        Machine machine = null;
        try {
            machine = machineRepository.findWithLockingByIdAndCreatedByAndActive(machineId,user, true);
        }catch (Exception e){
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina izvrsava drugu akciju, zauzeta je"));
            if(isScheduled){
                errorService.addError("masina izvrsava drugu akciju, zauzeta je",machineId,userId,ActionEnum.RESTART);
            }
            return;
        }

        if(machine == null){
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina nije aktivna"));
            if(isScheduled){
                errorService.addError("masina nije aktivna",machineId,userId,ActionEnum.RESTART);
            }
            return;
        }
        machine = entityManager.merge(machine);
        if(!machine.getStatus().equalsIgnoreCase("RUNNING")){
            this.simpMessagingTemplate.convertAndSend("/topic/messages/" + userId, new Message("server", "masina ne moze biti restartovana, nije u running stanju"));
            if(isScheduled){
                errorService.addError("masina ne moze biti restartovana, nije u running stanju",machineId,userId,ActionEnum.RESTART);
            }
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
