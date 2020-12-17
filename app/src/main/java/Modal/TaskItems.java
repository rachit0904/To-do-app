package Modal;

public class TaskItems {
    private String task;
    private int id;
    public TaskItems() {
    }

    public TaskItems(String task,int id) {
        this.task = task;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

}