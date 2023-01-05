package rs.raf.rafcloud.actions;


import org.springframework.orm.ObjectOptimisticLockingFailureException;

public class MachineAction extends Thread{

    private Long machineId;
    private Long userId;
    private AbstractAction abstractAction;

    public MachineAction(Long machineId, Long userId, AbstractAction abstractAction ) {
        this.machineId = machineId;
        this.userId = userId;
        this.abstractAction = abstractAction;
    }

//    public void run()  za optimistic
//    {
//        try {
//            abstractAction.doMachineAction(machineId,userId);
//        }catch (ObjectOptimisticLockingFailureException e){
//            System.out.println(e + "BBBBBBBBBBBBBBBBBBBB ");
//        }
//    }

    public void run()
    {
        abstractAction.doMachineAction(machineId,userId);
    }


}
