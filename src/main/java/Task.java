import java.sql.Timestamp;

public class Task
{
    private Timestamp time;
    private boolean toggleVent;
    private boolean toggleLight;
    private int taskId;

    public Task(Timestamp time, boolean toggleVent, boolean toggleLight, int taskId) {
        this.time = time;
        this.toggleVent = toggleVent;
        this.toggleLight = toggleLight;
        this.taskId = taskId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public boolean isToggleVent() {
        return toggleVent;
    }

    public void setToggleVent(boolean toggleVent) {
        this.toggleVent = toggleVent;
    }

    public boolean isToggleLight() {
        return toggleLight;
    }

    public void setToggleLight(boolean toggleLight) {
        this.toggleLight = toggleLight;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
