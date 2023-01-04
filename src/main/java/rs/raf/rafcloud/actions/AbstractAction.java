package rs.raf.rafcloud.actions;

import org.springframework.transaction.annotation.Transactional;
import rs.raf.rafcloud.model.Machine;

public interface AbstractAction {

    @Transactional
    Machine doMachineAction(Long machineId, Long userId);
}
