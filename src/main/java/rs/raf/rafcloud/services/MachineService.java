package rs.raf.rafcloud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.rafcloud.actions.MyBean;
import rs.raf.rafcloud.actions.StartAction;
import rs.raf.rafcloud.dtos.CreateMachineDto;
import rs.raf.rafcloud.model.Machine;
import rs.raf.rafcloud.model.User;
import rs.raf.rafcloud.repositories.MachineRepository;
import rs.raf.rafcloud.repositories.UserRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class MachineService implements IService<Machine,Long>{

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    private final MyBean myBean;
    @Autowired
    public MachineService(MachineRepository machineRepository, UserRepository userRepository, MyBean myBean) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.myBean = myBean;
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
//        machine = entityManager.merge(machine);
        StartAction startAction = new StartAction(machineId, userId, this.myBean);
        startAction.start();
//        if(!machine.getStatus().equals("STOPPED")) throw new RuntimeException("Masina nije u stanju stopped i ne moze biti startovana");
    }
}
