import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("CSC303 Lab 02 - Java ready");

        // --- Task 2A: Declarations and arithmetic ---
        String deviceName = "Sensor-A";
        int battery = 15;
        boolean connected = false;
        double ratio = 90 / 60.0;

        System.out.println(deviceName + " | battery=" + battery);
        System.out.println("Connected: " + connected);
        System.out.println("Ratio: " + ratio);
        System.out.println("Phone-B".equals(deviceName));

        // --- Task 2B: Decision method calls ---
        System.out.println("15: " + batteryStatus(15));
        System.out.println("20: " + batteryStatus(20));
        System.out.println("80: " + batteryStatus(80));

        // --- Task 2C: Process several readings ---
        int[] readings = {15, 80, 45};
        int lowCount = 0;

        for (int reading : readings) {
            String status = batteryStatus(reading);
            System.out.println("Reading " + reading + ": " + status);
            if (status.equals("LOW")) lowCount++;
        }

        System.out.println("Number of LOW readings: " + lowCount);
        System.out.printf(java.util.Locale.ROOT, "Average: %.2f%n", averageBattery(readings));

        // --- Task 3: Arrays and Collections ---
        System.out.println("\n--- Task 3 Output ---");
        List<String> names = new ArrayList<>();
        names.add("Sensor-A");
        names.add("Phone-B");
        names.add("Tablet-C");

        for (String name : names) System.out.println(name);
        System.out.println("Size: " + names.size());
        System.out.println("Contains Phone-B: " + names.contains("Phone-B"));
        names.remove("Tablet-C");
        System.out.println("After removal size: " + names.size());

        // --- Task 4: Classes and Encapsulation ---
        System.out.println("\n--- Task 4 Output ---");
        List<Device> devices = new ArrayList<>();
        devices.add(new Device("Sensor-A", 15));
        devices.add(new Device("Phone-B", 80));
        devices.add(new Device("Tablet-C", 45));

        for (Device d : devices) {
            System.out.println(d.summary() + " - Status: " + batteryStatus(d.getBattery()));
        }

        // --- Task 5: Inheritance, Interfaces, and Polymorphism ---
        System.out.println("\n--- Task 5 Output ---");
        MobileDevice phone = new MobileDevice("Phone-B", 80);
        Connectable connection = phone;
        System.out.println("isConnected before: " + connection.isConnected());
        connection.connect();
        System.out.println("isConnected after: " + connection.isConnected());
        Device item = phone;
        System.out.println(item.summary());

        // --- Task 6: Exception Handling and Testing ---
        System.out.println("\n--- Task 6 Output (Parsing inputs) ---");
        String[] inputs = {"80", "abc", "120"};
        for (String text : inputs) {
            try {
                int value = Integer.parseInt(text);
                System.out.println(text + ": " + batteryStatus(value));
            } catch (NumberFormatException ex) {
                System.out.println(text + ": enter a whole number");
            } catch (IllegalArgumentException ex) {
                System.out.println(text + ": " + ex.getMessage());
            }
        }

        // --- Task 7: Integrate and Demonstrate ---
        System.out.println("\n--- Task 7 Output (Integrated Inventory) ---");
        List<Device> finalInventory = new ArrayList<>();
        finalInventory.add(new Device("Sensor-A", 15));

        // Create Phone-B as a MobileDevice and connect it
        MobileDevice myPhone = new MobileDevice("Phone-B", 80);
        myPhone.connect();
        finalInventory.add(myPhone);

        finalInventory.add(new Device("Tablet-C", 45));

        // Print all summaries through the List<Device> reference
        for (Device d : finalInventory) {
            System.out.println(d.summary());
        }
    }

    static String batteryStatus(int battery) {
        if (battery < 0 || battery > 100) {
            throw new IllegalArgumentException("Battery out of range");
        }
        if (battery < 20) {
            return "LOW";
        } else {
            return "READY";
        }
    }

    static double averageBattery(int[] readings) {
        if (readings == null || readings.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        double sum = 0;
        for (int reading : readings) {
            if (reading < 0 || reading > 100) {
                throw new IllegalArgumentException("Battery out of range in array");
            }
            sum += reading;
        }
        return sum / readings.length;
    }
}