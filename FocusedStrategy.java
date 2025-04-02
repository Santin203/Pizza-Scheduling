import java.util.LinkedList;
import java.util.Queue;

public class FocusedStrategy implements ISchedulerStrategy {
    private static ISchedulerStrategy instance = null;
    private Queue<ITask> taskQueue; //

    private FocusedStrategy() {
        this.taskQueue = new LinkedList<>();
    }

    public static ISchedulerStrategy getInstance() {
        if (instance == null) {
            instance = new FocusedStrategy();
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
        // Fix for bug queue
        assignTask(resource, task);
    }

    @Override
    public void execute(IResource resource) {

    }

    @Override
    public void addTask(ITask task) {
        taskQueue.add(task);
    }

    @Override
    public void removeTask(ITask task) {
        taskQueue.remove(task);
    }

    @Override
    public void updateDoneResources(IResource resource, ITask task) {
        resource.setAvailable(true);
        resource.setTask(null);
    }
}


