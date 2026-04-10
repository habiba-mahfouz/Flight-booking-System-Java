package dsproject;

import java.util.Scanner;

public class DSProject {

    private static Boolean running = true;
    private static int tries = 0;
    private static int maxTries = 3;

    private static Scanner input = new Scanner(System.in);
    private static Registration acc = new Registration();

    // Build a Passenger object from user data array
    // userData: [name, email, phone, password, age, gender, nationalId, passport]
    private static Passenger buildPassengerFromUserData(String[] userData) {
        String name = userData[0];
        String email = userData[1];
        String phone = userData[2];
        String ageStr = userData[4];
        String gender = userData[5];
        String nationalId = userData[6];
        String passport = userData[7];

        int age = 0;
        try {
            if (ageStr != null && !ageStr.trim().isEmpty()) {
                age = Integer.parseInt(ageStr.trim());
            }
        } catch (NumberFormatException e) {
            age = 0;
        }

        return new Passenger(name, nationalId, age, gender, passport, phone, email);
    }

    // Run tickets booking flow using an existing Scanner and current passenger
    public static void runTickets(Scanner input, Passenger passenger) {

        // Initialize seat manager (you can change seat limits if you want)
        // For example: 20 economy seats, 10 first class seats
        Search seatSearch = new Search(20, 10);

        // Get number of tickets (requested seats)
        System.out.println("How many tickets do you want to buy?");
        int NumberOfTickets = input.nextInt();
        input.nextLine(); // consume newline

        // Get departure city
        System.out.println("What is your departure city?\nCairo\nAlexandria\nAswan");
        String departure = input.nextLine().toLowerCase();
        while (!"cairo".equals(departure) && !"alexandria".equals(departure) && !"aswan".equals(departure)) {
            System.out.println("Invalid input. Please enter the city again:");
            departure = input.nextLine().toLowerCase();
        }

        // Get destination city
        System.out.println("What is your destination?\nLondon\nParis\nDubai");
        String destination = input.nextLine().toLowerCase();
        while (!"london".equals(destination) && !"paris".equals(destination) && !"dubai".equals(destination)) {
            System.out.println("Invalid input. Please enter the city again:");
            destination = input.nextLine().toLowerCase();
        }

        // Get ticket class
        System.out.println("Please enter your ticket class\nEconomy\nFirst");
        String ticketClass = input.nextLine().toLowerCase();
        while (!"economy".equals(ticketClass) && !"first".equals(ticketClass)) {
            System.out.println("Invalid input. Please enter the class again:");
            ticketClass = input.nextLine().toLowerCase();
        }

        // Try to book seats in the chosen class using the Search + Queue
        int bookedSeats = seatSearch.bookSeats(ticketClass, NumberOfTickets);

        // If no seats were booked, stop here (no services and no payment)
        if (bookedSeats == 0) {
            System.out.println("Cannot continue with booking because there are no available seats.");
            return;
        }

        // Create ticket object (price per one ticket for this route and class)
        BuyTickets b1 = new BuyTickets(departure, destination, bookedSeats, ticketClass);

        // Service menu
        System.out.println("Welcome to service menu");

        System.out.println("Do you want a meal?\n1-Yes\n2-No");
        int meal = input.nextInt();

        System.out.println("Do you want wifi?\n1-Yes\n2-No");
        int wifi = input.nextInt();

        System.out.println("Do you want a comfort seat?\n1-Yes\n2-No");
        int seat = input.nextInt();

        System.out.println("Do you want entertainment?\n1-Yes\n2-No");
        int entertainment = input.nextInt();

        // Create services object
        Service S1 = new Service(meal, wifi, seat, entertainment);

        // Payment menu
        System.out.println("Welcome to payment menu");

        // Consume leftover newline from last nextInt()
        input.nextLine();

        System.out.println("Enter method of payment\nVisa Card\nCash");
        String PaymentMethod = input.nextLine().toLowerCase();

        if (!"visa card".equals(PaymentMethod) && !"cash".equals(PaymentMethod)) {
            System.out.println("You have entered invalid input, so your payment method is set to cash.");
            PaymentMethod = "cash";
        }

        // Create payment object with ticket price and services price
        // IMPORTANT: use bookedSeats, not the original requested number
        Payment p1 = new Payment(b1.TotalTicketBill(), S1.getTotalServicesPrice(), bookedSeats, PaymentMethod);

        // If Visa is chosen, ask for Visa number
        if ("visa card".equals(PaymentMethod)) {
            System.out.println("Enter your Visa number:");
            int VisaNumber = input.nextInt();
            // You can add Visa number validation here if needed
        }

        // Show total bill and confirmation
        p1.DisplayPrice();
        p1.PaymentConfirmation();

        // Save this flight into the passenger's flight history
        double totalPrice = p1.gettotalprice();
        StringBuilder serviceBuilder = new StringBuilder();
        if (meal == 1)
            serviceBuilder.append("Meal, ");
        if (wifi == 1)
            serviceBuilder.append("WiFi, ");
        if (seat == 1)
            serviceBuilder.append("Comfort Seat, ");
        if (entertainment == 1)
            serviceBuilder.append("Entertainment, ");
        String serviceStr = serviceBuilder.length() > 0 ? serviceBuilder.substring(0, serviceBuilder.length() - 2)
                : "None";

        passenger.AddFlight(departure, destination, ticketClass, serviceStr, PaymentMethod, totalPrice);
        System.out.println("Your flight has been saved to your flight history.");
    }

    // helper method: complete missing optional data (age, gender, National ID,
    // Passport number)
    private static void completeMissingData(String[] data, Scanner input) {
        // data: 0:name, 1:email, 2:phone, 3:password, 4:age, 5:gender, 6:NationalID,
        // 7:Passport_no
        String[] fieldNames = { "Age", "Gender", "National ID", "Passport Number" };

        for (int i = 4; i <= 7 && i < data.length; i++) {
            while (data[i] == null || data[i].trim().isEmpty()) {
                System.out.print(fieldNames[i - 4] + ": ");
                String value = input.nextLine().trim();

                if (value.isEmpty()) {
                    System.out.println(
                            "This field is required! Please enter " + fieldNames[i - 4].toLowerCase() + " again.");
                } else {
                    data[i] = value;
                }
            }
        }
    }

    // After login or signup: show user actions menu (buy ticket / view old flights
    // / exit)
    private static void userActionsMenu(Passenger passenger, Scanner input) {
        boolean inUserMenu = true;
        while (inUserMenu) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Buy ticket");
            System.out.println("2. View old flights");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            if (!input.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                input.nextLine();
                continue;
            }

            int choice = input.nextInt();
            input.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    // Before buying ticket, check if account data is complete
                    String[] userData = acc.getUserData(passenger.getName());
                    if (userData != null && !acc.checkData(userData)) {
                        System.out.println(
                                "Your account data is not complete (age / gender / National ID / Passport number).");
                        System.out.println("Please complete the following required fields:");

                        completeMissingData(userData, input);

                        if (acc.checkData(userData)) {
                            System.out.println("Your account data has been completed successfully.");
                        }
                    }
                    // Now proceed to buy tickets
                    runTickets(input, passenger);
                    break;
                case 2:
                    passenger.displayOldFlights();
                    break;
                case 3:
                    System.out.println("Exiting user menu.");
                    inUserMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        }
    }

    public static void Login_Main(Registration acc, Scanner input) {

        // identifier : check if this field is empty or not
        System.out.print(" Identifier: ");
        String identifier = "";
        boolean validIdentifier = false;

        while (!validIdentifier) {
            identifier = input.nextLine().trim();

            if (identifier.isEmpty()) {
                System.out.print("This field is required! Try again.\nIdentifier: ");
            } else {
                validIdentifier = true;
            }
        }

        // password : check if this field is empty or not
        System.out.print(" Password: ");
        String password_l = "";
        boolean validPassword = false;

        while (!validPassword) {
            password_l = input.nextLine().trim();

            if (password_l.isEmpty()) {
                System.out.print("This field is required! Try again.\nPassword: ");
            } else {
                validPassword = true;
            }
        }

        int login_result = acc.Login(identifier, password_l);

        switch (login_result) {
            case 0:
                tries++;
                System.out.println("No account found. Please sign up first. (" + tries + "/" + maxTries + ")");
                break;
            case 1:
                tries++;
                System.out.println("Wrong password or identifier. (" + tries + "/" + maxTries + ")");
                break;
            case 2:
                System.out.println("Account found, you are logged in.");

                String[] userData = acc.getUserData(identifier);
                Passenger passenger = buildPassengerFromUserData(userData);

                // After successful login, show user menu (buy ticket / view old flights / exit)
                userActionsMenu(passenger, input);

                running = false;
                break;
        }
    }

    public static void Signup_Main(Registration acc, Scanner input) {
        // user name : check if this field empty or not
        System.out.print(" Username: ");
        String name = "";
        boolean validName = false;

        while (!validName) {
            name = input.nextLine().trim();

            if (name.isEmpty()) {
                System.out.print("This field is required! Try again.\nUser name: ");
            } else {
                validName = true;
            }
        }

        // email
        System.out.print(" Email: ");
        String email = "";
        boolean validEmail = false;

        while (!validEmail) {
            email = input.nextLine().trim();

            if (email.isEmpty()) {
                System.out.print("This field is required! Try again.\nEmail: ");
            } else if (!email.matches(".*@.*\\.com$")) {
                System.out.print("Invalid email (must contain \"@\" and \".com\").\nEmail: ");
            } else {
                validEmail = true;
            }
        }

        // phone number
        System.out.print(" Phone number: ");
        String phone = "";
        boolean validPhone = false;

        while (!validPhone) {
            phone = input.nextLine().trim();

            if (phone.isEmpty()) {
                System.out.print("This field is required! Try again.\nPhone number: ");
            } else if (!phone.matches("\\d{11}")) {
                System.out.print("Invalid phone number (must consist of 11 digits).\nPhone number: ");
            } else {
                validPhone = true;
            }
        }

        // age
        System.out.print(" Age: ");
        String age = input.nextLine().trim();

        // gender
        System.out.print(" Gender: ");
        String gender = input.nextLine().trim();

        // national id
        System.out.print(" National ID: ");
        String nationalId = input.nextLine().trim();

        // passport number
        System.out.print(" Passport Number: ");
        String PassportNumber = input.nextLine().trim();

        String finalAge = age.isEmpty() ? null : age;
        String finalGender = gender.isEmpty() ? null : gender;
        String finalNationalId = nationalId.isEmpty() ? null : nationalId;
        String finalPassport = PassportNumber.isEmpty() ? null : PassportNumber;

        // password
        System.out.print(" Password: ");
        String password_s = "";
        boolean validPassword_s = false;

        while (!validPassword_s) {
            password_s = input.nextLine().trim();

            if (password_s.isEmpty()) {
                System.out.print("This field is required! Try again.\nPassword: ");
            } else {
                validPassword_s = true;
            }
        }

        // confirm password
        System.out.print(" Confirm password: ");
        String confirm = "";
        boolean validConfirm = false;

        while (!validConfirm) {
            confirm = input.nextLine().trim();

            if (confirm.isEmpty()) {
                System.out.print("This field is required! Try again.\nConfirm password: ");
            } else if (!password_s.equals(confirm)) {
                System.out.print("Passwords do not match. Try again.\nConfirm password: ");
            } else {
                validConfirm = true;
            }
        }

        int signup_result = acc.Signup(name, email, phone, password_s, finalAge, finalGender, finalNationalId,
                finalPassport);

        switch (signup_result) {
            case -1:
                tries++;
                System.out.println("Username already exists. Try again. (" + tries + "/" + maxTries + ") ");
                break;
            case 3:
                System.out.println("Account already exists. Please log in.");
                break;
            case 1:
                System.out.println("Account created successfully.");

                String[] newUserData = acc.getUserData(name);
                Passenger passenger = buildPassengerFromUserData(newUserData);

                // After successful signup, show user menu (buy ticket / view old flights /
                // exit)
                userActionsMenu(passenger, input);

                running = false;
                break;
        }
    }

    public static void main(String[] args) {
        acc.DataSet();

        while (running && tries < maxTries) {
            System.out.println("\n--- Welcome ---");
            System.out.print(" 1. Log in \t 2. Sign up \t 3. Exit \n Enter your choice: ");

            if (!input.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                input.nextLine();
                continue;
            }

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    Login_Main(acc, input);
                    break;
                case 2:
                    Signup_Main(acc, input);
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
                    break;
            }

            if (tries >= maxTries) {
                System.out.println("\nMax attempts reached! System exiting.");
            }
        }
        input.close();
    }
}