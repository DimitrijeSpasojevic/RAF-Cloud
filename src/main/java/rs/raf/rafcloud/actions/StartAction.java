package rs.raf.rafcloud.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.rafcloud.model.Machine;
import rs.raf.rafcloud.model.User;
import rs.raf.rafcloud.repositories.MachineRepository;
import rs.raf.rafcloud.repositories.UserRepository;


public class StartAction extends Thread{


    private Long machineId;
    private Long userId;
    private MyBean myBean;

    public StartAction( Long machineId, Long userId,MyBean myBean) {
        this.machineId = machineId;
        this.userId = userId;
        this.myBean = myBean;
    }

    public void run()
    {
        myBean.startMachine(machineId,userId);
    }


}
