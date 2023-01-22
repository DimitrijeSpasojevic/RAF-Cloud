package rs.raf.rafcloud.actions;

import org.springframework.transaction.annotation.Transactional;

public interface AbstractAction {

    @Transactional
    void doMachineAction(Long machineId, Long userId, Boolean isScheduled);
}
