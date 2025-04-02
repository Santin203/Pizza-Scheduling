public interface IResource {
    boolean isAvailable();
    void workOnTask();
    void setAvailable(boolean available);
    void setTask(ITask task);
    ITask getTask();
    IOrder getCurrentOrder();
}
