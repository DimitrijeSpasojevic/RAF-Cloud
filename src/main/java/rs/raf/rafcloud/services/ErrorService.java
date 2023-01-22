package rs.raf.rafcloud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.rafcloud.actions.ActionEnum;
import rs.raf.rafcloud.dtos.ErrorFromFront;
import rs.raf.rafcloud.model.ErrorMessage;
import rs.raf.rafcloud.repositories.ErrorRepository;
import rs.raf.rafcloud.repositories.MachineRepository;
import rs.raf.rafcloud.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ErrorService implements IService<ErrorMessage,Long>{

    private final MachineRepository machineRepository;
    private final UserRepository userRepository;
    private final ErrorRepository errorRepository;

    @Autowired
    public ErrorService(MachineRepository machineRepository, UserRepository userRepository, ErrorRepository errorRepository) {
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
        this.errorRepository = errorRepository;
    }

    @Override
    public <S extends ErrorMessage> S save(S err) {
        return errorRepository.save(err);
    }

    @Override
    public ErrorMessage findById(Long var1) {
        return null;
    }

    public List<ErrorMessage> findAllByUserId(Long userId) {
        return errorRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteById(Long var1) {

    }

    public ErrorMessage addError(String errText, Long machineId, Long userId, ActionEnum actionEnum){
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrorText(errText);
        errorMessage.setMachineId(machineId);
        errorMessage.setUserId(userId);
        errorMessage.setLocalDateTime(LocalDateTime.now());
        errorMessage.setActionEnum(actionEnum);
        return this.save(errorMessage);
    }
}
