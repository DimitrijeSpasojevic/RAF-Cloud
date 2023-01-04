package rs.raf.rafcloud.actions;


public class MachineAction extends Thread{

    private Long machineId;
    private Long userId;
    private AbstractAction abstractAction;

    public MachineAction(Long machineId, Long userId, AbstractAction abstractAction ) {
        this.machineId = machineId;
        this.userId = userId;
        this.abstractAction = abstractAction;
    }

    public void run()
    {
        abstractAction.doMachineAction(machineId,userId);
    }


}
