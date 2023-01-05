package rs.raf.rafcloud.actions;


import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.UnexpectedRollbackException;

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
        try {
            abstractAction.doMachineAction(machineId,userId);
        }catch (UnexpectedRollbackException e){
            System.out.println("Rollback se desio");
        }
    }


}
