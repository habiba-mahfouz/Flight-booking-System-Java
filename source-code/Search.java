
package dsproject;

// This class manages seat booking for economy and first classes using two queues.
public class Search {

    private Queue economyQueue;
    private Queue firstQueue;
    private int economyRemaining;
    private int firstRemaining;

    // Initialize seats for each class and fill queues with seat labels
    public Search(int economySeats, int firstSeats) {
        this.economyQueue = new Queue();
        this.firstQueue = new Queue();
        this.economyRemaining = economySeats;
        this.firstRemaining = firstSeats;

        // Fill economy queue with seats: E1, E2, ...
        for (int i = 1; i <= economySeats; i++) {
            String seatLabel = "E" + i;
            economyQueue.EnQueue(new String[]{seatLabel});
        }

        // Fill first queue with seats: F1, F2, ...
        for (int i = 1; i <= firstSeats; i++) {
            String seatLabel = "F" + i;
            firstQueue.EnQueue(new String[]{seatLabel});
        }
    }

    /**
     * Try to book seats in the given class.
     *
     * @param seatClass      "economy" or "first"
     * @param requestedSeats number of seats the user wants
     * @return number of seats that were actually booked
     */
    public int bookSeats(String seatClass, int requestedSeats) {
        if (requestedSeats <= 0) {
            System.out.println("Number of seats must be greater than zero.");
            return 0;
        }

        Queue targetQueue;
        int remaining;
        String className;

        if (seatClass.equalsIgnoreCase("economy")) {
            targetQueue = economyQueue;
            remaining = economyRemaining;
            className = "economy";
        } else if (seatClass.equalsIgnoreCase("first")) {
            targetQueue = firstQueue;
            remaining = firstRemaining;
            className = "first";
        } else {
            System.out.println("Invalid class. Please choose 'economy' or 'first'.");
            return 0;
        }

        // Case: no seats at all in this class
        if (remaining == 0 || targetQueue.isEmpty()) {
            System.out.println("No seats available in " + className + " class.");
            return 0;
        }

        int toBook = Math.min(requestedSeats, remaining);
        int booked = 0;

        System.out.print("Booked seats in " + className + " class: ");
        while (booked < toBook && !targetQueue.isEmpty()) {
            Node seatNode = targetQueue.DeQueue();
            if (seatNode != null) {
                String seatLabel = seatNode.getData()[0];  // seat label (E1, F2, ...)
                System.out.print(seatLabel + " ");
                booked++;
            }
        }
        System.out.println(); // new line

        // Update remaining counters
        if (className.equals("economy")) {
            economyRemaining -= booked;
        } else {
            firstRemaining -= booked;
        }

        // Messages based on how many seats were actually booked
        if (booked == requestedSeats) {
            System.out.println("Successfully booked " + booked + " " + className + " seat(s).");
        } else if (booked > 0) {
            System.out.println("Only " + booked + " " + className + " seat(s) could be booked. You requested " + requestedSeats + ".");
            System.out.println("The limit is reached. No more seats available in " + className + " class.");
        } else {
            System.out.println("No seats available in " + className + " class.");
        }

        return booked;
    }
}