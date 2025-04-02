public class Oven extends Thread implements IOven {
    private int id;
    private boolean available;
    private ITask task;

    public Oven(int id) {
        this.id = id;
        this.available = true;
        this.task = null;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void workOnTask() {
        task.bake(1);
    }

    public void setTask(ITask task) {
        this.task = task;
    }

    @Override
    public ITask getTask() {
        return task;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public IOrder getCurrentOrder() {
        return task.getOrder();
    }
}

