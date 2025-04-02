public class Driver implements IDriver {
    private int id;
    private boolean available;
    private IOrder order;
    private int deliveryTime;

    public Driver(int id) {
        this.id = id;
        this.available = true;
        this.order = null;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void assignOrder(IOrder order) {
        this.order = order;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public void workOnTask() {
        order.deliver(1);
    }

    @Override
    public IOrder getOrder() {
        return order;
    }

    @Override
    public void setOrder(IOrder order) {
        this.order = order;
    }
}

