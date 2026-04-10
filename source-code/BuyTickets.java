package dsproject;

public class BuyTickets {

    private String From;
    private String to;
    private int NumberOfTickets;
    private String SeatClass;

    public BuyTickets(String From, String to, int NumberOfTickets, String SeatClass) {
        this.From = From;
        this.to = to;
        this.NumberOfTickets = NumberOfTickets;
        this.SeatClass = SeatClass;
    }

    double TicketPrice;

    // Calculate ticket price per one ticket based on route and class
    public double Price() {

        switch (From) {
            case "cairo":
                switch (to) {
                    case "london":
                        switch (SeatClass) {
                            case "economy":
                                TicketPrice = 311.71;
                                break;
                            case "first":
                                TicketPrice = 4001.71;
                                break;
                        }
                        break;
                    case "paris":
                        switch (SeatClass) {
                            case "economy":
                                TicketPrice = 331.16;
                                break;
                            case "first":
                                TicketPrice = 3978.41;
                                break;
                        }
                        break;
                    case "dubai":
                        switch (SeatClass) {
                            case "economy":
                                TicketPrice = 170.7;
                                break;
                            case "first":
                                TicketPrice = 2450.14;
                                break;
                        }
                        break;
                }
                break;
            case "alexandria":
                switch (to) {
                    case "london":
                        switch (SeatClass) {
                            case "economy":
                                TicketPrice = 408.11;
                                break;
                            case "first":
                                TicketPrice = 5245.36;
                                break;
                        }
                        break;
                    case "paris":
                        switch (SeatClass) {
                            case "economy":
                                TicketPrice = 441.50;
                                break;
                            case "first":
                                TicketPrice = 4743.73;
                                break;
                        }
                        break;
                    case "dubai":
                        switch (SeatClass) {
                            case "economy":
                                TicketPrice = 154.38;
                                break;
                            case "first":
                                TicketPrice = 2219.06;
                                break;
                        }
                        break;
                }
                break;
            case "aswan":
                switch (to) {
                    case "london":
                        switch (SeatClass) {
                            case "economy":
                                TicketPrice = 654.14;
                                break;
                            case "first":
                                TicketPrice = 8419.54;
                                break;
                        }
                        break;
                    case "paris":
                        switch (SeatClass) {
                            case "economy":
                                TicketPrice = 542.28;
                                break;
                            case "first":
                                TicketPrice = 5834.89;
                                break;
                        }
                        break;
                    case "dubai":
                        switch (SeatClass) {
                            case "economy":
                                TicketPrice = 433.93;
                                break;
                            case "first":
                                TicketPrice = 6246.21;
                                break;
                        }
                        break;
                }
                break;
        }
        return TicketPrice;
    }

    // Returns price per ticket (Payment will multiply by NumberOfTickets)
    public double TotalTicketBill() {
        return Price();
    }

}