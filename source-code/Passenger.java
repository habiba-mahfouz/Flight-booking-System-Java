package dsproject;

public class Passenger {
    private String name;
    private String nationalId;
    private int age;
    private String gender;
    private String passportNumber;
    private String phone;
    private String email;

    // Linked list to store this passenger's flights
    private Single_LL flights = new Single_LL();

    public Passenger(String n, String nId, int age, String gender, String passNum, String phone, String email) {
        name = n;
        nationalId = nId;
        this.age = age;
        this.gender = gender;
        passportNumber = passNum;
        this.phone = phone;
        this.email = email;
    }

    public Passenger() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Add a new flight to this passenger's flight history
    // Data: from, to, seat class, service, payment method, price
    public void AddFlight(String from, String to, String seatClass, String service, String paymentMethod,
            double price) {
        if (flights == null) {
            flights = new Single_LL();
        }
        String[] flightData = {
                from,
                to,
                seatClass,
                service,
                paymentMethod,
                String.valueOf(price)
        };
        flights.append(flightData);
    }

    // Display all old flights for this passenger
    public void displayOldFlights() {
        if (flights == null) {
            System.out.println("No previous flights.");
        } else {
            flights.displayFlights();
        }
    }

    @Override
    public String toString() {
        return "Name: " + this.name +
                "\nNationalId: " + nationalId +
                "\nAge: " + age +
                "\nGender: " + gender +
                "\nPassportNumber: " + passportNumber +
                "\nPhone: " + phone +
                "\nEmail: " + email;
    }

    // You can use this to display a single flight if needed
    public void DisplayOldFlight(String From, String To, String SeatClass, Double Price) {
        System.out.println("From: " + From +
                ", To: " + To +
                ", Seat class: " + SeatClass +
                ", Price: " + Price);
    }
}