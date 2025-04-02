public interface ISchedulerStrategy {
    void assignTask(IResource resource, ITask task);
    void execute(IResource resource);
    void addTask(ITask task);
    void removeTask(ITask task);
    void updateDoneResources(IResource resource, ITask task);
    void assignTaskChef(IResource resource, ITask task);
}
