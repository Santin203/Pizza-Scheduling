import java.util.List;

public class Restaurant implements IRestaurant {
    private static IRestaurant instance;
    private List<IOrder> orders;
    private List<IChef> chefs;
    private List<IOven> ovens;
    private List<IDriver> drivers;
    private ISchedulerStrategy scheduler;
    private int currentMinute;

    public Restaurant(List<IOrder> orders, RestaurantConfig config) {
        this.orders = orders;
        this.chefs = config.getChefs();
        this.ovens = config.getOvens();
        this.drivers = config.getDrivers();
        this.scheduler = config.getChefStrategy();
        this.currentMinute = 0;
    }

    public static IRestaurant getInstance(List<IOrder> orders, RestaurantConfig config) {
        if (instance == null) {
            instance = new Restaurant(orders, config);
        }
        return instance;
    }

    public void runSimulation() {
        while (!allOrdersDelivered()) {
            currentMinute++;
            System.out.println("==== MINUTE " + currentMinute);

            checkTimeSpent();

            assignChefs();

            checkDonePreparing();
            
            assignOvens();

            checkDoneBaking();

            assignDrivers();

            workResources();

            checkDoneResources();

            updateOrdersToOvenWaiting();

            updateOrdersToDriverWaiting();

            updateOrdersToDelivered();

            checkDoneResources();

            printOrdersState();

            printResourcesState();
        }
    }

    private void updateOrdersToOvenWaiting() {
        for (IOrder order : orders) {
            ((Order) order).updateToOvenWaiting();
        }
    }
    
    private void updateOrdersToDriverWaiting() {
        for (IOrder order : orders) {
            ((Order) order).updateToDriverWaiting();
        }
    }

    private void updateOrdersToDelivered() {
        for (IOrder order : orders) {
            ((Order) order).updateToDelivered();
        }
    }

    private boolean allOrdersDelivered() {
        return orders.stream().allMatch(order -> order.getState() == OrderState.DELIVERED); //
    }

    private void assignChefs() {
        for (IChef chef : chefs) {
            if (chef.isAvailable()) {
                for (IOrder order : orders) {
                    if (!order.areAllPizzasPrepared() && order.isPrepared() == false) {
                        for (ITask task : order.getPizzas()) {
                            if (task.isAvailable() && chef.isAvailable()) {
                                scheduler.assignTaskChef((IResource) chef, task);
                                order.updateState(OrderState.PREPARING);
                                break;
                            }
                        }
                    }
                    
                }
            }
        }
    }

    private void assignOvens() {
        for (IOven oven : ovens) {
            if (oven.isAvailable()) {
                for (IOrder order : orders) {
                    if (order.areAllPizzasPrepared()) {
                        for (ITask task : order.getPizzas()) {
                            if (task.isAvailable() && oven.isAvailable()) {
                                scheduler.assignTask(oven, task);
                                order.updateState(OrderState.OVEN_PREPARING);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void assignDrivers() {
        for (IDriver driver : drivers) {
            if (driver.isAvailable()) {
                for (IOrder order : orders) {
                    if (order.getState() == OrderState.DRIVER_WAITING && order.areAllPizzasBaked() && order.isAvailable() == true) {
                        driver.assignOrder(order);
                        driver.setAvailable(false);
                        order.setAvailable(false);
                        break;
                    }
                }
            }
        }
    }

    private void workResources() {
        for (IChef chef : chefs) {
            if (chef.getTask() != null) {
                chef.workOnTask();
            }
        }
        for (IOven oven : ovens) {
            if (oven.getTask() != null) {
                oven.workOnTask();
            }
        }
        for (IDriver driver : drivers) {
            if (driver.getOrder() != null) {
                driver.workOnTask();
            }
        }
    }

    private void checkDoneResources() {
        for (IChef chef : chefs) {
            if (chef.getTask() != null) {
                ITask task = chef.getTask();
                if (task != null && ((IPizza) task).getChefTimeRemaining() == 0) {
                    scheduler.updateDoneResources(chef, task);
                }
            }
        }
        for (IOven oven : ovens) {
            if (oven.getTask() != null) {
                ITask task = oven.getTask();
                if (((IPizza) task).getBakeTimeRemaining() == 0) {
                    oven.setAvailable(true);
                    oven.setTask(null);
                }
            }
        }
        for (IDriver driver : drivers) {
            if (driver.getOrder() != null) {
                if (((IOrder) driver.getOrder()).getState() == OrderState.DELIVERED) {
                    driver.setAvailable(true);
                    driver.setOrder(null);
                }
            }
        }
    }

    private void checkDonePreparing() {
        for (IOrder order : orders) {
            if (order.areAllPizzasPrepared() && (order.isPrepared() == false)) {
                order.setPrepared(true);
                for (ITask task : order.getPizzas()) {
                    task.setAvailable(true);
                }
            }
        }
    }

    private void checkDoneBaking() {
        for (IOrder order : orders) {
            if (order.areAllPizzasBaked() && (order.isBaked() == false)) {
                order.setBaked(true);
            }
        }
    }

    private void checkTimeSpent() {
        for (IResource chef : chefs) {
            scheduler.execute(chef);
        }

    }

    private void printOrdersState() {
        for (IOrder order : orders) {
            order.printOrder();
        }
    }

    private void printResourcesState() {
        for (int i = 0; i < chefs.size(); i++) {
            IChef chef = chefs.get(i);
            if (chef.getTask() != null && scheduler instanceof RoundRobinChefStrategy) {
                System.out.println("Chef" + i + "," + (chef.getCurrentOrder() != null ? chef.getCurrentOrder().getCustomerName() : "None") + "," + (((RoundRobinChefStrategy) scheduler).getQuantum() - chef.getTimeSpent()));
            } else if (chef.getTask() != null) {
                System.out.println("Chef" + i + "," + (chef.getCurrentOrder() != null ? chef.getCurrentOrder().getCustomerName() : "None")); //
            } else {
                System.out.println("Chef" + i + ",None");
            }
        }
        for (int i = 0; i < ovens.size(); i++) {
            IOven oven = ovens.get(i);
            if (oven.getTask() != null) {
                System.out.println("Oven" + i + "," + (oven.getCurrentOrder() != null ? oven.getCurrentOrder().getCustomerName() : "None")); //
            } else {
                System.out.println("Oven" + i + ",None");
            }
            
        }
        for (int i = 0; i < drivers.size(); i++) {
            IDriver driver = drivers.get(i);
            System.out.println("Driver" + i + "," + (driver.getOrder() != null ? driver.getOrder().getCustomerName() : "None")); //
        }
    }
}

