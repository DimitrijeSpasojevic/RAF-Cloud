package rs.raf.rafcloud.actions;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.rafcloud.model.Machine;
import rs.raf.rafcloud.model.User;
import rs.raf.rafcloud.repositories.MachineRepository;
import rs.raf.rafcloud.repositories.UserRepository;


public class StartAction extends Thread{

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    private Long machineId;
    private Long userId;

    public StartAction(MachineRepository machineRepository, UserRepository userRepository, Long machineId, Long userId) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.machineId = machineId;
        this.userId = userId;
    }

    public void run()
    {
        startMachine(machineId,userId);
    }

    public Machine startMachine(Long machineId, Long userId){
        System.out.print("StartAction running");
        User user = userRepository.findByUserId(userId);
        Machine machine = machineRepository.findWithLockingByIdAndCreatedBy(machineId,user);
        System.out.println("------USAOOO--------");
        try {
            sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        machine.setStatus("RUNNING");
        System.out.print("StartAction finished");
        this.machineRepository.saveAndFlush(machine);
        return machine;
    }
}
