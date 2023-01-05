package rs.raf.rafcloud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.rafcloud.model.ErrorMessage;

import java.util.List;

@Repository
public interface ErrorRepository extends JpaRepository<ErrorMessage, Long> {
    List<ErrorMessage> findAllByMachineId(Long id);
    List<ErrorMessage> findAllByUserId(Long id);
}
