public class Pizza implements IPizza {
    private int id;
    private IOrder order;
    private int chefTimeRemaining;
    private int bakeTimeRemaining;
    private boolean available;
    private boolean delivered;


    public Pizza(int id, IOrder order, int chefTimeRemaining, int bakeTimeRemaining) {
        this.id = id;
        this.order = order;
        this.bakeTimeRemaining = bakeTimeRemaining;
        this.chefTimeRemaining = chefTimeRemaining;
        this.available = true;
        this.delivered = false;
    }

    public boolean isReadyToBake() {
        return chefTimeRemaining == 0;
    }

    public boolean isReadyToDeliver() {
        return bakeTimeRemaining == 0 && chefTimeRemaining == 0;
    }

    public boolean isDelivered() {
        return delivered;
    }
    
    public void prepare(int minutes) {
        if (chefTimeRemaining > 0) {
            chefTimeRemaining -= minutes;
        }
    }

    public void bake(int minutes) {
        if (bakeTimeRemaining > 0) {
            bakeTimeRemaining -= minutes;
        }
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public int getId() { return id; }
    public IOrder getOrder() { return order; }
    public int getChefTimeRemaining() { return chefTimeRemaining; }
    public int getBakeTimeRemaining() { return bakeTimeRemaining; }
    public boolean isAvailable() { return available; }
}

