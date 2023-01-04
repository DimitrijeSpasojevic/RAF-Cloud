package rs.raf.rafcloud.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.rafcloud.dtos.ErrorFromFront;
import rs.raf.rafcloud.model.MyError;
import rs.raf.rafcloud.repositories.ErrorRepository;
import rs.raf.rafcloud.repositories.MachineRepository;
import rs.raf.rafcloud.repositories.UserRepository;

import java.util.List;

@Service
public class ErrorService implements IService<MyError,Long>{

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
    public <S extends MyError> S save(S err) {
        return errorRepository.save(err);
    }

    @Override
    public MyError findById(Long var1) {
        return null;
    }

    public List<MyError> findAllByUserId(Long userId) {
        return errorRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteById(Long var1) {

    }

    public MyError addError(ErrorFromFront errorFromFront, Long userId){
        MyError myError = new MyError();
        myError.setErrorText(errorFromFront.getErrorText());
        myError.setMachineId(errorFromFront.getMachineId());
        myError.setUserId(userId);
        return this.save(myError);
    }
}
