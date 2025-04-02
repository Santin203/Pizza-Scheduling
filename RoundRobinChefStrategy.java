import java.util.LinkedList;
import java.util.Queue;

public class RoundRobinChefStrategy implements ISchedulerStrategy {
    private static ISchedulerStrategy instance = null;
    private int quantum;
    private Queue<ITask> taskQueue; //

    public RoundRobinChefStrategy(int quantum) {
        this.quantum = quantum;
        this.taskQueue = new LinkedList<>();
    }

    public static ISchedulerStrategy getInstance(int quantum) {
        if (instance == null) {
            instance = new RoundRobinChefStrategy(quantum);
        }
        return instance;
    }

    @Override
    public void assignTask(IResource resource, ITask task) {
        if (resource.isAvailable() && task.isAvailable()) {
            resource.setTask(task);
            resource.setAvailable(false);
            task.setAvailable(false);
        }
    }

    @Override
    public void assignTaskChef(IResource resource, ITask task) {
        if (resource.isAvailable() && task.isAvailable() && !taskQueue.isEmpty()) {
            resource.setTask(task);
            resource.setAvailable(false);
            task.setAvailable(false);
            taskQueue.remove();
        }
    }

    @Override
    public void execute(IResource resource) {
        checkChefTimeSpent((IChef) resource);
    }

    private void checkChefTimeSpent(IChef chef) {
        ITask task = chef.getTask();
        if (chef.getTimeSpent() >= quantum) {
            if (((IPizza) task).getChefTimeRemaining() > 0) {
                taskQueue.add(task);
            }
            task.setAvailable(true);
            ITask nextTask = getNextTask();
            chef.setTask(nextTask);
            if (nextTask != null) {
                chef.setAvailable(false);
                nextTask.setAvailable(false);
                updateOrderState(nextTask);
            } else {
                chef.setAvailable(true);
            }
            updateOrderState(task);
        }
    }

    @Override
    public void updateDoneResources(IResource resource, ITask task) {
        this.removeTask(task);
        execute(resource);
    }

    public ITask getNextTask() {
        return taskQueue.isEmpty() ? null : taskQueue.poll();
    }

    @Override
    public void addTask(ITask task) {
        taskQueue.add(task);
    }

    private void updateOrderState(ITask task) {
        IOrder order = task.getOrder();
        for (ITask t : order.getPizzas()) {
            if (!(t.isAvailable())) {
                order.updateState(OrderState.PREPARING);
                return;
            }
        }
        order.updateState(OrderState.CHEF_WAITING);

    }

    public int getQuantum() {
        return quantum;
    }

    @Override
    public void removeTask(ITask task) {
        taskQueue.remove(task);
        task.setAvailable(true);
    }
}

