package dsproject;

public class Payment {

    private double TicketPrice;
    private double ServicePrice;
    private int NumberOfTickets;
    private double TotalPrice;
    private String paymentMethod;

    public Payment() {
    }

    public Payment(double Ticket, double Service, int NumberOfTickets, String paymentMethod) {
        this.TicketPrice = Ticket;
        this.ServicePrice = Service;
        this.NumberOfTickets = NumberOfTickets;
        this.paymentMethod = paymentMethod;
    }

    // Calculate total price for all tickets + services
    public double setTotalPrice() {
        TotalPrice = (TicketPrice + ServicePrice) * NumberOfTickets;
        return TotalPrice;
    }

    public double gettotalprice() {
        return TotalPrice;
    }

    // Calculate and display total bill
    public void DisplayPrice() {
        setTotalPrice();
        System.out.println("Your Total Bill is " + gettotalprice() + " $");
    }

    // Display payment confirmation message
    public void PaymentConfirmation() {
        System.out.println("Payment successful using " + paymentMethod + ".");
    }

}