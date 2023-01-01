package rs.raf.rafcloud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.rafcloud.model.Machine;
import rs.raf.rafcloud.model.User;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findAllByCreatedBy(User user);

    Machine findByIdAndCreatedBy(Long machineId, User user);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional(propagation = Propagation.MANDATORY)
    Machine findWithLockingByIdAndCreatedBy(Long machineId, User user);
}
