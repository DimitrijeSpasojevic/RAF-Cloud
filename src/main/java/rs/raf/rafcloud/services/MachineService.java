package rs.raf.rafcloud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.rafcloud.actions.MachineAction;
import rs.raf.rafcloud.actions.RestartAction;
import rs.raf.rafcloud.actions.StartAction;
import rs.raf.rafcloud.actions.StopAction;
import rs.raf.rafcloud.dtos.CreateMachineDto;
import rs.raf.rafcloud.model.Machine;
import rs.raf.rafcloud.model.User;
import rs.raf.rafcloud.repositories.MachineRepository;
import rs.raf.rafcloud.repositories.UserRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MachineService implements IService<Machine,Long>{

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    private final StartAction startAction;
    private final StopAction stopAction;
    private final RestartAction restartAction;
    @Autowired
    public MachineService(MachineRepository machineRepository, UserRepository userRepository, StartAction startAction, StopAction stopAction, RestartAction restartAction) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.startAction = startAction;
        this.stopAction = stopAction;
        this.restartAction = restartAction;
    }

    @Override
    public <S extends Machine> S save(S machine) {
        return machineRepository.save(machine);
    }

    @Override
    public Machine findById(Long var1) {
        return null;
    }

    @Override
    public void deleteById(Long var1) {

    }

    public List<Machine> findAll() {
        return machineRepository.findAll();
    }

    public List<Machine> findAllByUserId(Long userId) {
        User user = userRepository.findByUserId(userId);
        return machineRepository.findAllByCreatedBy(user);
    }

    public Machine createMachine(CreateMachineDto createMachineDto, Long userId){
        User user = userRepository.findByUserId(userId);
        Machine machine = new Machine();
        machine.setActive(true);
        machine.setCreatedBy(user);
        machine.setStatus("STOPPED");
        machine.setName(createMachineDto.getName());
        machine.setCreationDate(Date.valueOf(LocalDate.now()));
        return this.save(machine);
    }

    public void startMachine(Long machineId, Long userId) {
        MachineAction machineAction = new MachineAction(machineId, userId, this.startAction);
        machineAction.start();
    }

    public void stopMachine(Long machineId, Long userId) {
        MachineAction machineAction = new MachineAction(machineId, userId, this.stopAction);
        machineAction.start();
    }

    public void restartMachine(Long machineId, Long userId) {
        MachineAction machineAction = new MachineAction(machineId, userId, this.restartAction);
        machineAction.start();
    }

    public void destroyMachine(Long machineId, Long userId) {
        Optional<Machine> machine = this.machineRepository.findById(machineId);
        if(!machine.get().getStatus().equalsIgnoreCase("STOPPED")) throw new RuntimeException("Ne moze biti obrisana");
        machine.get().setActive(false);
        machineRepository.saveAndFlush(machine.get());
    }
}
