import java.util.ArrayList;
import java.util.List;

public class Order implements IOrder {
    private String customerName;
    private List<Pizza> pizzas;
    private int priority;
    private OrderState state;
    private int deliveryTime;
    private boolean prepared;
    private boolean baked;
    private boolean available;

    public Order(String customerName, int numPizzas, int chefTime, int bakeTime, int deliveryTime, int priority) {
        this.customerName = customerName;
        this.pizzas = initPizzas(numPizzas, chefTime, bakeTime);
        this.deliveryTime = deliveryTime;
        this.priority = priority;
        this.state = OrderState.PENDING;
        this.prepared = false;
        this.baked = false;
        this.available = true;
    }

    public void updateState(OrderState state) {
        this.state = state;
    }

    @Override
    public void printOrder() {
        System.out.println(customerName + "," + state + "," + getPizzasDone() + "," + getPizzasPending() + "," + getTimeLeft());
    }



    public int getPizzasDone() {
        switch (state) {
            case PENDING:
            case PREPARING:
            case CHEF_WAITING:
                return (int) pizzas.stream().filter(Pizza::isReadyToBake).count(); //
            case OVEN_WAITING:
            case OVEN_PREPARING:
                return (int) pizzas.stream().filter(Pizza::isReadyToDeliver).count(); //
            case DRIVER_WAITING:
                return (int) pizzas.stream().filter(Pizza::isDelivered).count(); //
            case DELIVERED:
                return pizzas.size();
            default:
                return 0;
        }
    }

    public int getPizzasPending() {
        switch (state) {
            case PENDING:
            case PREPARING:
            case CHEF_WAITING:
                return (int) pizzas.stream().filter(pizza -> !pizza.isReadyToBake()).count(); //
            case OVEN_WAITING:
            case OVEN_PREPARING:
                return (int) pizzas.stream().filter(pizza -> !pizza.isReadyToDeliver()).count(); //
            case DRIVER_WAITING:
                return (int) pizzas.stream().filter(pizza -> !pizza.isDelivered()).count(); //
            default:
                return 0;
        }
    }


    public int getTimeLeft() {
        switch (state) {
            case PENDING:
            case PREPARING:
            case CHEF_WAITING:
                return pizzas.stream().mapToInt(Pizza::getChefTimeRemaining).sum(); //
            case OVEN_WAITING:
            case OVEN_PREPARING:
                return pizzas.stream().mapToInt(Pizza::getBakeTimeRemaining).sum(); //
            case DRIVER_WAITING:
                return deliveryTime;
            default:
                return 0;
        }
    }

    public void updateToOvenWaiting() {
        if (pizzas.stream().allMatch(pizza -> pizza.getChefTimeRemaining() == 0 && (state == OrderState.PREPARING || state == OrderState.CHEF_WAITING))) {
            updateState(OrderState.OVEN_WAITING);
        }
    }
    
    public void updateToDriverWaiting() {
        if (pizzas.stream().allMatch(pizza -> pizza.getBakeTimeRemaining() == 0 && state == OrderState.OVEN_PREPARING)) {
            updateState(OrderState.DRIVER_WAITING);
        }
    }

    public void updateToDelivered() {
        if (deliveryTime == 0) {
            updateState(OrderState.DELIVERED);
        }
    }

    public void deliver(int minutes) {
        if (deliveryTime > 0) {
            deliveryTime -= minutes;
        }
    }

    @Override
    public boolean areAllPizzasPrepared() {
        return pizzas.stream().allMatch(Pizza::isReadyToBake);
    }
    
    @Override
    public boolean areAllPizzasBaked() {
        return pizzas.stream().allMatch(Pizza::isReadyToDeliver);
    }

    @Override
    public boolean isPrepared() {
        return prepared;
    }

    @Override
    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    @Override
    public boolean isBaked() {
        return baked;
    }

    @Override
    public void setBaked(boolean baked) {
        this.baked = baked;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    private List<Pizza> initPizzas(int numPizzas, int chefTime, int bakeTime) {
        List<Pizza> pizzas = new ArrayList<>();
        for (int i = 0; i < numPizzas; i++) {
            pizzas.add(new Pizza(i, this, chefTime, bakeTime));
        }
        return pizzas;
    }
    

    @Override
    public List<Pizza> getPizzas() { return pizzas; }

    @Override
    public String getCustomerName() { return customerName; }
    public int getNumPizzas() { return pizzas.size(); }
    public int getPriority() { return priority; }
    public OrderState getState() { return state; }
    public int getDeliveryTime() { return deliveryTime; }

}

