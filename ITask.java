public interface ITask {
    void prepare(int minutes);
    void bake(int minutes);
    boolean isAvailable();
    void setAvailable(boolean available);
    IOrder getOrder();
}
