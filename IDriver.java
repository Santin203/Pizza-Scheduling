public interface IDriver {
    void assignOrder(IOrder order);
    void setOrder(IOrder order);
    boolean isAvailable();
    void setAvailable(boolean available);
    void workOnTask();
    IOrder getOrder();
}
