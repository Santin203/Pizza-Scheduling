import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("No arguments provided. Please provide the required parameters.");
            System.exit(1);
        }

        // Call takeOrder to parse the arguments
        RestaurantConfig config = Cashier.takeOrder(args);

        // Get orders from file
        List<IOrder> orders = Cashier.parseOrders(config.getInputFile(), config.getChefTime(), config.getBakeTime());

        // Sort orders by priority
        Cashier.sortOrders(orders);

        // Create a restaurant instance with the config
        IRestaurant restaurant = Restaurant.getInstance(orders, config);

        // Add tasks to the chef queue
        Cashier.addTasksQueue(orders, config);

        // Start the simulation
        restaurant.runSimulation();
    }
}


