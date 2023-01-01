package rs.raf.rafcloud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.rafcloud.actions.StartAction;
import rs.raf.rafcloud.dtos.CreateMachineDto;
import rs.raf.rafcloud.model.Machine;
import rs.raf.rafcloud.model.User;
import rs.raf.rafcloud.repositories.MachineRepository;
import rs.raf.rafcloud.repositories.UserRepository;

import java.util.List;

@Service
public class MachineService implements IService<Machine,Long>{

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository, UserRepository userRepository) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
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
        return this.save(machine);
    }

    public String startMachine(Long machineId, Long userId) {
//        machine = entityManager.merge(machine);
        StartAction startAction = new StartAction(machineRepository, userRepository, machineId, userId);
        startAction.start();
//        if(!machine.getStatus().equals("STOPPED")) throw new RuntimeException("Masina nije u stanju stopped i ne moze biti startovana");
        return "pocelo";
    }

}
