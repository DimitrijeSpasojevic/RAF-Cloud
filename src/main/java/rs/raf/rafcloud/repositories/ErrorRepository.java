package rs.raf.rafcloud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.rafcloud.model.MyError;

import java.util.List;

@Repository
public interface ErrorRepository extends JpaRepository<MyError, Long> {
    List<MyError> findAllByMachineId(Long id);
    List<MyError> findAllByUserId(Long id);
}
