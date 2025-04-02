import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Cashier {
    
    /*
     * Prompt: Implement sortOrders() by priority
     */
    public static void sortOrders(List<IOrder> orders) {
        // Sort orders based on priority (lower number = higher priority)
        orders.sort(Comparator.comparingInt(IOrder::getPriority));
    }
    
    /*
     * Prompt: Generate a parsing method for the parameters provided to the cashier
     */
    public static RestaurantConfig takeOrder(String[] args) {
        String inputFile = "";
        int availableOvens = 0;
        int availableChefs = 0;
        int availableDrivers = 0;
        int bakeTime = 0;
        int chefTime = 0;
        String chefStrategy = "";
        int chefQuantum = 0;
        
        for (int i = 0; i < args.length; i += 2) {
            if (i + 1 >= args.length) {
                System.err.println("Missing value for parameter: " + args[i]);
                System.exit(1);
            }
            
            switch (args[i]) {
                case "--input-file":
                    inputFile = args[i + 1];
                    break;
                case "--available-ovens":
                    availableOvens = Integer.parseInt(args[i + 1]);
                    break;
                case "--available-chefs":
                    availableChefs = Integer.parseInt(args[i + 1]);
                    break;
                case "--available-drivers":
                    availableDrivers = Integer.parseInt(args[i + 1]);
                    break;
                case "--bake-time":
                    bakeTime = Integer.parseInt(args[i + 1]);
                    break;
                case "--chef-time":
                    chefTime = Integer.parseInt(args[i + 1]);
                    break;
                case "--chef-strategy":
                    chefStrategy = args[i + 1];
                    break;
                case "--chef-quantum":
                    chefQuantum = Integer.parseInt(args[i + 1]);
                    break;
                default:
                    System.err.println("Unknown parameter: " + args[i]);
                    System.exit(1);
            }
        }

        return new RestaurantConfig(inputFile, availableOvens, availableChefs, availableDrivers, bakeTime, chefTime, chefStrategy, chefQuantum);
    }

    /*
     * Prompt: Implement getOrders() method to parse orders from the input file. Sample input file format:
     * Person,NumPizzas,DeliveryTime,Priority
     * John,2,30,1
     */
    public static List<IOrder> parseOrders(String inputFile, int chefTime, int bakeTime) {
        List<IOrder> orders = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    System.err.println("Invalid order format: " + line);
                    continue;
                }

                String customerName = parts[0].trim();
                int numPizzas = Integer.parseInt(parts[1].trim());
                int deliveryTime = Integer.parseInt(parts[2].trim());
                int priority = Integer.parseInt(parts[3].trim());

                IOrder order = new Order(customerName, numPizzas, chefTime, bakeTime, deliveryTime, priority);
                orders.add(order);
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            System.exit(1);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in input file: " + e.getMessage());
            System.exit(1);
        }

        return orders;
    }


    public static void addTasksQueue(List<IOrder> orders, RestaurantConfig config) {
        ISchedulerStrategy scheduler = config.getChefStrategy();
        for (IOrder order : orders) {
            for (ITask task : order.getPizzas()) {
                scheduler.addTask(task);
            }
        }
    }


}
