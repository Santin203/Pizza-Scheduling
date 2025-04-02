public class Chef implements IChef {
    private int id;
    private ISchedulerStrategy strategy;
    private boolean available;
    private ITask task;
    private int timeSpentOnTask;

    public Chef(int id, ISchedulerStrategy strategy) {
        this.id = id;
        this.strategy = strategy;
        this.available = true;
        this.task = null;
        this.timeSpentOnTask = 0;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void workOnTask() {
        task.prepare(1);
        this.timeSpentOnTask++;
    }

    public void setTask(ITask task) {
        this.task = task;
        this.timeSpentOnTask = 0;
    }

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

    @Override
    public int getTimeSpent() {
        return timeSpentOnTask;
    }
}

