import java.util.List;

public interface IOrder {
    void printOrder();
    String getCustomerName();
    List<Pizza> getPizzas();
    OrderState getState();
    void updateState(OrderState state);
    int getPriority();
    void deliver(int minutes);
    boolean areAllPizzasBaked();
    boolean areAllPizzasPrepared();
    boolean isPrepared();
    void setPrepared(boolean prepared);
    boolean isBaked();
    void setBaked(boolean baked);
    boolean isAvailable();
    void setAvailable(boolean available);
}

