import java.util.ArrayList;
import java.util.List;

public class RestaurantConfig {
    private String inputFile;
    private int availableOvens;
    private int availableChefs;
    private int availableDrivers;
    private int bakeTime;
    private int chefTime;
    private String chefStrategy;
    private int chefQuantum;

    public RestaurantConfig(String inputFile, int availableOvens, int availableChefs, int availableDrivers,
                            int bakeTime, int chefTime, String chefStrategy, int chefQuantum) {
        this.inputFile = inputFile;
        this.availableOvens = availableOvens;
        this.availableChefs = availableChefs;
        this.availableDrivers = availableDrivers;
        this.bakeTime = bakeTime;
        this.chefTime = chefTime;
        this.chefStrategy = chefStrategy;
        this.chefQuantum = chefQuantum;
    }

    public String getInputFile() { return inputFile; }

    public List<IOven> getOvens() { 
        List<IOven> ovens = new ArrayList<>();
        for (int i = 0; i < availableOvens; i++) {
            ovens.add(new Oven(i));
        }
        return ovens;
    }

    public List<IChef> getChefs() {
        List<IChef> chefs = new ArrayList<>();
        for (int i = 0; i < availableChefs; i++) {
            chefs.add(new Chef(i, this.getChefStrategy()));
        }
        return chefs;
     }

    public List<IDriver> getDrivers() {
        List<IDriver> drivers = new ArrayList<>();
        for (int i = 0; i < availableDrivers; i++) {
            drivers.add(new Driver(i));
        }
        return drivers;
    }

    public ISchedulerStrategy getChefStrategy() {
        if ("FOCUSED".equalsIgnoreCase(chefStrategy)) {
            return FocusedStrategy.getInstance();
        } else if ("RR".equalsIgnoreCase(chefStrategy)) {
            return RoundRobinChefStrategy.getInstance(chefQuantum);
        } else {
            throw new IllegalArgumentException("Invalid chef strategy: " + chefStrategy); //
        }
    }

    public int getAvailableOvens() { return availableOvens; }
    public int getAvailableChefs() { return availableChefs; }
    public int getAvailableDrivers() { return availableDrivers; }
    public int getBakeTime() { return bakeTime; }
    public int getChefTime() { return chefTime; }
    public int getChefQuantum() { return chefQuantum; }
}

