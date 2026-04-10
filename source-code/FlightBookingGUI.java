package dsproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FlightBookingGUI extends JFrame {

    private Registration acc;
    private Passenger currentPassenger;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Color scheme - shades of blue
    private final Color PRIMARY_BLUE = new Color(25, 118, 210); // Bright blue
    private final Color SECONDARY_BLUE = new Color(33, 150, 243); // Light blue
    private final Color DARK_BLUE = new Color(13, 71, 161); // Dark blue
    private final Color LIGHT_BLUE = new Color(144, 202, 249); // Very light blue
    private final Color BACKGROUND_BLUE = new Color(227, 242, 253); // Pale blue background
    private final Color ACCENT_BLUE = new Color(66, 165, 245); // Accent blue

    // Panels
    private JPanel loginPanel;
    private JPanel signupPanel;
    private JPanel menuPanel;
    private JPanel bookingPanel;

    public FlightBookingGUI() {
        acc = new Registration();
        acc.DataSet();

        setTitle("Flight Booking System");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createLoginPanel();
        createSignupPanel();
        createMenuPanel();
        createBookingPanel();

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }

    // Login Screen
    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(BACKGROUND_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 100;

        // Subtitle
        JLabel subtitleLabel = new JLabel("Welcome to Flight Booking System", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        subtitleLabel.setForeground(PRIMARY_BLUE);
        gbc.gridy = 1;
        loginPanel.add(subtitleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(75, 12, 12, 12);
        JLabel identifierLabel = new JLabel("Email/Phone/Name:");
        identifierLabel.setFont(new Font("Arial", Font.BOLD, 14));
        identifierLabel.setForeground(DARK_BLUE);
        loginPanel.add(identifierLabel, gbc);

        gbc.gridx = 1;
        JTextField identifierField = new JTextField(22);
        identifierField.setFont(new Font("Arial", Font.PLAIN, 14));
        identifierField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_BLUE, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        loginPanel.add(identifierField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(12, 12, 12, 12);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(DARK_BLUE);
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(22);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_BLUE, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 12, 12, 12);
        JButton loginButton = createStyledButton("Login", PRIMARY_BLUE);
        loginButton.setPreferredSize(new Dimension(250, 45));
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(12, 12, 12, 12);
        JButton goToSignupButton = createStyledButton("Create New Account", SECONDARY_BLUE);
        goToSignupButton.setPreferredSize(new Dimension(250, 40));
        loginPanel.add(goToSignupButton, gbc);

        loginButton.addActionListener(e -> {
            String identifier = identifierField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (identifier.isEmpty() || password.isEmpty()) {
                showStyledMessage("Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int result = acc.Login(identifier, password);

            switch (result) {
                case 0:
                    showStyledMessage("No account found with these credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case 1:
                    showStyledMessage("Incorrect password or identifier!", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case 2:
                    String[] userData = acc.getUserData(identifier);
                    currentPassenger = buildPassengerFromUserData(userData);
                    showStyledMessage("Welcome " + currentPassenger.getName() + "!", "Login Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "menu");
                    identifierField.setText("");
                    passwordField.setText("");
                    break;
            }
        });

        goToSignupButton.addActionListener(e -> cardLayout.show(mainPanel, "signup"));

        mainPanel.add(loginPanel, "login");
    }

    // Signup Screen
    private void createSignupPanel() {
        signupPanel = new JPanel(new BorderLayout());
        signupPanel.setBackground(BACKGROUND_BLUE);

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(BACKGROUND_BLUE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(DARK_BLUE);
        titlePanel.add(titleLabel);
        signupPanel.add(titlePanel, BorderLayout.NORTH);

        // Form Panel with ScrollPane
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = { "Name:", "Email:", "Phone:", "Password:", "Confirm Password:", "Age:", "Gender:",
                "National ID:", "Passport Number:" };
        JTextField[] fields = new JTextField[9];
        JComboBox<String> genderCombo = null;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 13));
            label.setForeground(DARK_BLUE);
            formPanel.add(label, gbc);

            gbc.gridx = 1;
            if (i == 3 || i == 4) {
                fields[i] = new JPasswordField(20);
                fields[i].setFont(new Font("Arial", Font.PLAIN, 13));
                fields[i].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(LIGHT_BLUE, 2),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                formPanel.add(fields[i], gbc);
            } else if (i == 6) {
                // Gender ComboBox
                genderCombo = new JComboBox<>(new String[] { "", "Male", "Female" });
                genderCombo.setFont(new Font("Arial", Font.PLAIN, 13));
                genderCombo.setBackground(Color.WHITE);
                formPanel.add(genderCombo, gbc);
            } else {
                fields[i] = new JTextField(20);
                fields[i].setFont(new Font("Arial", Font.PLAIN, 13));
                fields[i].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(LIGHT_BLUE, 2),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                formPanel.add(fields[i], gbc);
            }
        }

        final JComboBox<String> finalGenderCombo = genderCombo;

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_BLUE);
        signupPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonsPanel.setBackground(BACKGROUND_BLUE);

        JButton signupButton = createStyledButton("Sign Up", PRIMARY_BLUE);
        signupButton.setPreferredSize(new Dimension(180, 45));
        buttonsPanel.add(signupButton);

        JButton backToLoginButton = createStyledButton("Back to Login", ACCENT_BLUE);
        backToLoginButton.setPreferredSize(new Dimension(180, 45));
        buttonsPanel.add(backToLoginButton);

        signupPanel.add(buttonsPanel, BorderLayout.SOUTH);

        signupButton.addActionListener(e -> {
            String name = fields[0].getText().trim();
            String email = fields[1].getText().trim();
            String phone = fields[2].getText().trim();
            String password = fields[3].getText().trim();
            String confirmPassword = fields[4].getText().trim();
            String age = fields[5].getText().trim();
            String gender = (String) finalGenderCombo.getSelectedItem();
            if (gender != null)
                gender = gender.trim();
            String nationalId = fields[7].getText().trim();
            String passport = fields[8].getText().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                showStyledMessage("Please fill required fields (Name, Email, Phone, Password)!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.matches(".*@.*\\.com$")) {
                showStyledMessage("Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!phone.matches("\\d{11}")) {
                showStyledMessage("Phone number must be 11 digits!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                showStyledMessage("Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int result = acc.Signup(name, email, phone, password,
                    age.isEmpty() ? null : age,
                    gender.isEmpty() ? null : gender,
                    nationalId.isEmpty() ? null : nationalId,
                    passport.isEmpty() ? null : passport);

            switch (result) {
                case 0:
                    showStyledMessage("An error occurred during signup!", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case 1:
                    String[] userData = acc.getUserData(name);
                    currentPassenger = buildPassengerFromUserData(userData);
                    showStyledMessage("Account created successfully! Welcome " + name, "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i] != null)
                            fields[i].setText("");
                    }
                    finalGenderCombo.setSelectedIndex(0);
                    cardLayout.show(mainPanel, "menu");
                    break;
                case 3:
                    showStyledMessage("Account already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        });

        backToLoginButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        mainPanel.add(signupPanel, "signup");
    }

    // Main Menu
    private void createMenuPanel() {
        menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(BACKGROUND_BLUE);

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(BACKGROUND_BLUE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        JLabel welcomeLabel = new JLabel("Main Menu");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 40));
        welcomeLabel.setForeground(DARK_BLUE);
        titlePanel.add(welcomeLabel);
        menuPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setBackground(BACKGROUND_BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JButton bookTicketButton = createMenuButton("Book New Ticket", PRIMARY_BLUE);
        gbc.gridy = 0;
        buttonsPanel.add(bookTicketButton, gbc);

        JButton viewFlightsButton = createMenuButton("View Previous Flights", SECONDARY_BLUE);
        gbc.gridy = 1;
        buttonsPanel.add(viewFlightsButton, gbc);

        JButton rateButton = createMenuButton("Rate Application", ACCENT_BLUE);
        gbc.gridy = 2;
        buttonsPanel.add(rateButton, gbc);

        JButton logoutButton = createMenuButton("Logout", DARK_BLUE);
        gbc.gridy = 3;
        buttonsPanel.add(logoutButton, gbc);

        menuPanel.add(buttonsPanel, BorderLayout.CENTER);

        bookTicketButton.addActionListener(e -> {
            String[] userData = acc.getUserData(currentPassenger.getName());
            if (userData != null && !acc.checkData(userData)) {
                // Show dialog to complete missing data
                JDialog completeDataDialog = new JDialog(this, "Complete Your Data", true);
                completeDataDialog.setSize(550, 500);
                completeDataDialog.setLocationRelativeTo(this);
                completeDataDialog.getContentPane().setBackground(BACKGROUND_BLUE);
                completeDataDialog.setLayout(new BorderLayout());

                JLabel titleLabel = new JLabel("Please Complete Your Account Data", SwingConstants.CENTER);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
                titleLabel.setForeground(DARK_BLUE);
                titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
                completeDataDialog.add(titleLabel, BorderLayout.NORTH);

                JPanel formPanel = new JPanel(new GridBagLayout());
                formPanel.setBackground(BACKGROUND_BLUE);
                formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
                GridBagConstraints gac = new GridBagConstraints();
                gac.insets = new Insets(10, 10, 10, 10);
                gac.fill = GridBagConstraints.HORIZONTAL;

                String[] labels = { "Age:", "Gender:", "National ID:", "Passport Number:" };
                JTextField[] fields = new JTextField[4];
                JComboBox<String> genderComboDialog = null;

                for (int i = 0; i < labels.length; i++) {
                    gac.gridx = 0;
                    gac.gridy = i;
                    JLabel label = new JLabel(labels[i]);
                    label.setFont(new Font("Arial", Font.BOLD, 14));
                    label.setForeground(DARK_BLUE);
                    formPanel.add(label, gac);

                    gac.gridx = 1;

                    if (i == 1) {
                        // Gender ComboBox
                        genderComboDialog = new JComboBox<>(new String[] { "", "Male", "Female" });
                        genderComboDialog.setFont(new Font("Arial", Font.PLAIN, 14));
                        genderComboDialog.setBackground(Color.WHITE);

                        // Pre-fill if data exists
                        if (userData[5] != null && !userData[5].trim().isEmpty()) {
                            genderComboDialog.setSelectedItem(userData[5]);
                        }

                        formPanel.add(genderComboDialog, gac);
                    } else {
                        fields[i] = new JTextField(15);
                        fields[i].setFont(new Font("Arial", Font.PLAIN, 14));
                        fields[i].setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(LIGHT_BLUE, 2),
                                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

                        // Pre-fill if data exists (Age=4, NationalID=6, Passport=7)
                        int dataIndex = (i == 0) ? 4 : (i == 2) ? 6 : 7;
                        if (userData[dataIndex] != null && !userData[dataIndex].trim().isEmpty()) {
                            fields[i].setText(userData[dataIndex]);
                        }

                        formPanel.add(fields[i], gac);
                    }
                }

                final JComboBox<String> finalGenderComboDialog = genderComboDialog;

                completeDataDialog.add(formPanel, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.setBackground(BACKGROUND_BLUE);
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

                JButton saveButton = createStyledButton("Save and Continue", PRIMARY_BLUE);
                saveButton.setPreferredSize(new Dimension(200, 40));
                buttonPanel.add(saveButton);

                completeDataDialog.add(buttonPanel, BorderLayout.SOUTH);

                saveButton.addActionListener(ev -> {
                    boolean allFilled = true;

                    // Check Age field
                    String ageValue = fields[0].getText().trim();
                    if (ageValue.isEmpty()) {
                        allFilled = false;
                    } else {
                        userData[4] = ageValue;
                    }

                    // Check Gender ComboBox
                    String genderValue = (String) finalGenderComboDialog.getSelectedItem();
                    if (genderValue == null || genderValue.trim().isEmpty()) {
                        allFilled = false;
                    } else {
                        userData[5] = genderValue.trim();
                    }

                    // Check National ID field
                    String nationalIdValue = fields[2].getText().trim();
                    if (nationalIdValue.isEmpty()) {
                        allFilled = false;
                    } else {
                        userData[6] = nationalIdValue;
                    }

                    // Check Passport field
                    String passportValue = fields[3].getText().trim();
                    if (passportValue.isEmpty()) {
                        allFilled = false;
                    } else {
                        userData[7] = passportValue;
                    }

                    if (!allFilled) {
                        showStyledMessage("Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Update passenger object
                    currentPassenger = buildPassengerFromUserData(userData);
                    showStyledMessage("Data updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    completeDataDialog.dispose();
                    cardLayout.show(mainPanel, "booking");
                });

                completeDataDialog.setVisible(true);
            } else {
                cardLayout.show(mainPanel, "booking");
            }
        });

        viewFlightsButton.addActionListener(e -> showFlightsDialog());

        rateButton.addActionListener(e -> showRateDialog());

        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                currentPassenger = null;
                cardLayout.show(mainPanel, "login");
            }
        });

        mainPanel.add(menuPanel, "menu");
    }

    // Booking Screen
    private void createBookingPanel() {
        bookingPanel = new JPanel(new BorderLayout());
        bookingPanel.setBackground(BACKGROUND_BLUE);

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(BACKGROUND_BLUE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        JLabel titleLabel = new JLabel("Book Flight Ticket");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(DARK_BLUE);
        titlePanel.add(titleLabel);
        bookingPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_BLUE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 50, 20, 50),
                BorderFactory.createLineBorder(LIGHT_BLUE, 2, true)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Number of tickets
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel ticketsLabel = new JLabel("Number of Tickets:");
        ticketsLabel.setFont(new Font("Arial", Font.BOLD, 15));
        ticketsLabel.setForeground(DARK_BLUE);
        formPanel.add(ticketsLabel, gbc);

        gbc.gridx = 1;
        SpinnerModel ticketsModel = new SpinnerNumberModel(1, 1, 10, 1);
        JSpinner ticketsSpinner = new JSpinner(ticketsModel);
        ticketsSpinner.setFont(new Font("Arial", Font.PLAIN, 15));
        ((JSpinner.DefaultEditor) ticketsSpinner.getEditor()).getTextField().setBackground(Color.WHITE);
        formPanel.add(ticketsSpinner, gbc);

        // Departure city
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel departureLabel = new JLabel("Departure City:");
        departureLabel.setFont(new Font("Arial", Font.BOLD, 15));
        departureLabel.setForeground(DARK_BLUE);
        formPanel.add(departureLabel, gbc);

        gbc.gridx = 1;
        String[] departures = { "Cairo", "Alexandria", "Aswan" };
        JComboBox<String> departureCombo = new JComboBox<>(departures);
        departureCombo.setFont(new Font("Arial", Font.PLAIN, 15));
        departureCombo.setBackground(Color.WHITE);
        formPanel.add(departureCombo, gbc);

        // Destination city
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel destinationLabel = new JLabel("Destination City:");
        destinationLabel.setFont(new Font("Arial", Font.BOLD, 15));
        destinationLabel.setForeground(DARK_BLUE);
        formPanel.add(destinationLabel, gbc);

        gbc.gridx = 1;
        String[] destinations = { "London", "Paris", "Dubai" };
        JComboBox<String> destinationCombo = new JComboBox<>(destinations);
        destinationCombo.setFont(new Font("Arial", Font.PLAIN, 15));
        destinationCombo.setBackground(Color.WHITE);
        formPanel.add(destinationCombo, gbc);

        // Ticket class
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel classLabel = new JLabel("Ticket Class:");
        classLabel.setFont(new Font("Arial", Font.BOLD, 15));
        classLabel.setForeground(DARK_BLUE);
        formPanel.add(classLabel, gbc);

        gbc.gridx = 1;
        String[] classes = { "Economy", "First" };
        JComboBox<String> classCombo = new JComboBox<>(classes);
        classCombo.setFont(new Font("Arial", Font.PLAIN, 15));
        classCombo.setBackground(Color.WHITE);
        formPanel.add(classCombo, gbc);

        bookingPanel.add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottomPanel.setBackground(BACKGROUND_BLUE);

        JButton continueButton = createStyledButton("Continue to Services", PRIMARY_BLUE);
        continueButton.setPreferredSize(new Dimension(200, 50));
        bottomPanel.add(continueButton);

        JButton backButton = createStyledButton("Back", ACCENT_BLUE);
        backButton.setPreferredSize(new Dimension(120, 50));
        bottomPanel.add(backButton);

        bookingPanel.add(bottomPanel, BorderLayout.SOUTH);

        continueButton.addActionListener(e -> {
            int tickets = (int) ticketsSpinner.getValue();
            String departure = ((String) departureCombo.getSelectedItem()).toLowerCase();
            String destination = ((String) destinationCombo.getSelectedItem()).toLowerCase();
            String ticketClass = ((String) classCombo.getSelectedItem()).toLowerCase();

            processBooking(tickets, departure, destination, ticketClass);
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        mainPanel.add(bookingPanel, "booking");
    }

    private void processBooking(int numberOfTickets, String departure, String destination, String ticketClass) {
        Search seatSearch = new Search(20, 10);
        int bookedSeats = seatSearch.bookSeats(ticketClass, numberOfTickets);

        if (bookedSeats == 0) {
            showStyledMessage("Sorry, no seats available!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        showServicesDialog(departure, destination, ticketClass, bookedSeats);
    }

    private void showServicesDialog(String departure, String destination, String ticketClass, int bookedSeats) {
        JDialog servicesDialog = new JDialog(this, "Select Services", true);
        servicesDialog.setSize(450, 450);
        servicesDialog.setLocationRelativeTo(this);
        servicesDialog.getContentPane().setBackground(BACKGROUND_BLUE);
        servicesDialog.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Select Additional Services", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(DARK_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        servicesDialog.add(titleLabel, BorderLayout.NORTH);

        JPanel servicesPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        servicesPanel.setBackground(BACKGROUND_BLUE);
        servicesPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JCheckBox mealCheck = createStyledCheckBox("Meal (+$20)");
        JCheckBox wifiCheck = createStyledCheckBox("WiFi (+$10)");
        JCheckBox seatCheck = createStyledCheckBox("Comfort Seat (+$20)");
        JCheckBox entertainmentCheck = createStyledCheckBox("Entertainment (+$30)");

        servicesPanel.add(mealCheck);
        servicesPanel.add(wifiCheck);
        servicesPanel.add(seatCheck);
        servicesPanel.add(entertainmentCheck);

        servicesDialog.add(servicesPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_BLUE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 25, 0));

        JButton confirmButton = createStyledButton("Confirm and Continue to Payment", PRIMARY_BLUE);
        confirmButton.setPreferredSize(new Dimension(300, 45));
        buttonPanel.add(confirmButton);

        servicesDialog.add(buttonPanel, BorderLayout.SOUTH);

        confirmButton.addActionListener(e -> {
            int meal = mealCheck.isSelected() ? 1 : 2;
            int wifi = wifiCheck.isSelected() ? 1 : 2;
            int seat = seatCheck.isSelected() ? 1 : 2;
            int entertainment = entertainmentCheck.isSelected() ? 1 : 2;

            Service service = new Service(meal, wifi, seat, entertainment);
            StringBuilder sb = new StringBuilder();
            if (meal == 1)
                sb.append("Meal, ");
            if (wifi == 1)
                sb.append("WiFi, ");
            if (seat == 1)
                sb.append("Comfort Seat, ");
            if (entertainment == 1)
                sb.append("Entertainment, ");
            String serviceStr = sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "None";

            servicesDialog.dispose();
            showPaymentDialog(departure, destination, ticketClass, bookedSeats, service, serviceStr);
        });

        servicesDialog.setVisible(true);
    }

    private void showPaymentDialog(String departure, String destination, String ticketClass, int bookedSeats,
            Service service, String serviceStr) {
        JDialog paymentDialog = new JDialog(this, "Payment", true);
        paymentDialog.setSize(500, 380);
        paymentDialog.setLocationRelativeTo(this);
        paymentDialog.getContentPane().setBackground(BACKGROUND_BLUE);
        paymentDialog.setLayout(new BorderLayout());

        BuyTickets buyTickets = new BuyTickets(departure, destination, bookedSeats, ticketClass);
        Payment payment = new Payment(buyTickets.TotalTicketBill(), service.getTotalServicesPrice(), bookedSeats,
                "cash");

        // Title
        JLabel titleLabel = new JLabel("Payment Information", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(DARK_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        paymentDialog.add(titleLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(BACKGROUND_BLUE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 40, 20, 40),
                BorderFactory.createLineBorder(LIGHT_BLUE, 2, true)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel totalLabel = new JLabel("Total Amount: $" + payment.setTotalPrice());
        totalLabel.setFont(new Font("Arial", Font.BOLD, 22));
        totalLabel.setForeground(PRIMARY_BLUE);
        gbc.gridy = 0;
        infoPanel.add(totalLabel, gbc);

        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        paymentMethodLabel.setFont(new Font("Arial", Font.BOLD, 16));
        paymentMethodLabel.setForeground(DARK_BLUE);
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 12, 8, 12);
        infoPanel.add(paymentMethodLabel, gbc);

        JComboBox<String> paymentCombo = new JComboBox<>(new String[] { "Cash", "Visa Card" });
        paymentCombo.setFont(new Font("Arial", Font.PLAIN, 15));
        paymentCombo.setBackground(Color.WHITE);
        gbc.gridy = 2;
        gbc.insets = new Insets(8, 12, 12, 12);
        infoPanel.add(paymentCombo, gbc);

        JLabel visaLabel = new JLabel("Visa Card Number:");
        visaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        visaLabel.setForeground(DARK_BLUE);
        visaLabel.setVisible(false);
        gbc.gridy = 3;
        gbc.insets = new Insets(8, 12, 8, 12);
        infoPanel.add(visaLabel, gbc);

        JTextField visaField = new JTextField(20);
        visaField.setFont(new Font("Arial", Font.PLAIN, 15));
        visaField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_BLUE, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        visaField.setVisible(false);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 12, 12, 12);
        infoPanel.add(visaField, gbc);

        paymentCombo.addItemListener(e -> {
            boolean isVisa = "Visa Card".equals(paymentCombo.getSelectedItem());
            visaLabel.setVisible(isVisa);
            visaField.setVisible(isVisa);
            paymentDialog.pack();
        });

        paymentDialog.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_BLUE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0));

        JButton confirmPaymentButton = createStyledButton("Confirm Payment", PRIMARY_BLUE);
        confirmPaymentButton.setPreferredSize(new Dimension(200, 45));
        buttonPanel.add(confirmPaymentButton);

        paymentDialog.add(buttonPanel, BorderLayout.SOUTH);

        confirmPaymentButton.addActionListener(e -> {
            String paymentMethod = (String) paymentCombo.getSelectedItem();
            if ("Visa Card".equals(paymentMethod)) {
                String visaNum = visaField.getText().trim();
                if (visaNum.isEmpty()) {
                    showStyledMessage("Please enter your Visa Card number!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            double totalPrice = payment.setTotalPrice();
            currentPassenger.AddFlight(departure, destination, ticketClass, serviceStr, paymentMethod, totalPrice);

            showStyledMessage("Booking successful!\nTotal Amount: $" + totalPrice, "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            paymentDialog.dispose();
            cardLayout.show(mainPanel, "menu");
        });

        paymentDialog.setVisible(true);
    }

    private void showRateDialog() {
        JDialog rateDialog = new JDialog(this, "Rate Application", true);
        rateDialog.setSize(450, 400);
        rateDialog.setLocationRelativeTo(this);
        rateDialog.getContentPane().setBackground(BACKGROUND_BLUE);
        rateDialog.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Rate Your Experience", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(DARK_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        rateDialog.add(titleLabel, BorderLayout.NORTH);

        JPanel ratePanel = new JPanel(new GridBagLayout());
        ratePanel.setBackground(BACKGROUND_BLUE);
        ratePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 30, 20, 30),
                BorderFactory.createLineBorder(LIGHT_BLUE, 2, true)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int[] finalWebRate = { 0 };

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel webRateLabel = new JLabel("Rating:", SwingConstants.CENTER);
        webRateLabel.setFont(new Font("Arial", Font.BOLD, 18));
        webRateLabel.setForeground(DARK_BLUE);
        ratePanel.add(webRateLabel, gbc);

        gbc.gridy = 1;
        JPanel starsPanel = createStarPanel(finalWebRate);
        ratePanel.add(starsPanel, gbc);

        rateDialog.add(ratePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_BLUE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0));

        JButton submitButton = createStyledButton("Submit Rating", PRIMARY_BLUE);
        submitButton.setPreferredSize(new Dimension(200, 45));
        buttonPanel.add(submitButton);

        rateDialog.add(buttonPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> {
            if (finalWebRate[0] == 0) {
                showStyledMessage("Please select a rating to submit.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            showStyledMessage(String.format("Thank you for your rating!\nYou rated %d stars \u2605", finalWebRate[0]),
                    "Rating Submitted", JOptionPane.INFORMATION_MESSAGE);
            rateDialog.dispose();
        });

        rateDialog.setVisible(true);
    }

    private JPanel createStarPanel(int[] rateHolder) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setBackground(BACKGROUND_BLUE);
        JLabel[] stars = new JLabel[5];
        String emptyStar = "\u2606";
        String filledStar = "\u2605";

        for (int i = 0; i < 5; i++) {
            int index = i;
            stars[i] = new JLabel(emptyStar);
            stars[i].setFont(new Font("Serif", Font.PLAIN, 40));
            stars[i].setForeground(new Color(255, 193, 7)); // Gold color
            stars[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            stars[i].addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    for (int j = 0; j <= index; j++)
                        stars[j].setText(filledStar);
                    for (int j = index + 1; j < 5; j++)
                        stars[j].setText(emptyStar);
                }

                public void mouseExited(MouseEvent e) {
                    for (int j = 0; j < rateHolder[0]; j++)
                        stars[j].setText(filledStar);
                    for (int j = rateHolder[0]; j < 5; j++)
                        stars[j].setText(emptyStar);
                }

                public void mouseClicked(MouseEvent e) {
                    rateHolder[0] = index + 1;
                    for (int j = 0; j < rateHolder[0]; j++)
                        stars[j].setText(filledStar);
                    for (int j = rateHolder[0]; j < 5; j++)
                        stars[j].setText(emptyStar);
                }
            });
            panel.add(stars[i]);
        }
        return panel;
    }


    private void showFlightsDialog() {
        JDialog flightsDialog = new JDialog(this, "Previous Flights", true);
        flightsDialog.setSize(600, 450);
        flightsDialog.setLocationRelativeTo(this);
        flightsDialog.getContentPane().setBackground(BACKGROUND_BLUE);
        flightsDialog.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Your Previous Flights", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(DARK_BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
        flightsDialog.add(titleLabel, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setMargin(new Insets(15, 15, 15, 15));
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(DARK_BLUE);

        StringBuilder flightsText = new StringBuilder();

        try {
            java.lang.reflect.Field flightsField = Passenger.class.getDeclaredField("flights");
            flightsField.setAccessible(true);
            Single_LL flightsList = (Single_LL) flightsField.get(currentPassenger);

            java.lang.reflect.Field headField = Single_LL.class.getDeclaredField("head");
            headField.setAccessible(true);
            Node head = (Node) headField.get(flightsList);

            if (head == null) {
                flightsText.append("No previous flights found.\n\n");
                flightsText.append("Book your first flight to see it here!");
            } else {
                flightsText.append(
                         "•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•\n\n");

                Node temp = head;
                int index = 1;

                while (temp != null) {
                    String[] flightData = temp.getData();

                    if (flightData.length >= 4) {
                        String from = flightData[0];
                        String to = flightData[1];
                        String seatClass = flightData[2];
                        String service = flightData.length > 3 ? flightData[3] : "N/A";
                        String payment = flightData.length > 4 ? flightData[4] : "N/A";
                        String price = flightData.length > 5 ? flightData[5] : flightData[3];

                        flightsText.append("flight : ").append(index).append("\n");
                        flightsText.append("to : ").append(to.toUpperCase()).append("\n");
                        flightsText.append("from : ").append(from.toUpperCase()).append("\n");
                        flightsText.append("class : ").append(seatClass.toUpperCase()).append("\n");
                        flightsText.append("service : ").append(service).append("\n");
                        flightsText.append("Payment way : ").append(payment).append("\n");
                        flightsText.append("total price : $").append(price).append("\n");
                        flightsText.append("\n\n\n");

                    }

                    temp = temp.getNext();
                    index++;
                }

                flightsText.append(
                        "•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•.•");
            }
        } catch (Exception ex) {
            flightsText.append("Error loading flights: ").append(ex.getMessage());
            ex.printStackTrace();
        }

        textArea.setText(flightsText.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 20, 20, 20),
                BorderFactory.createLineBorder(LIGHT_BLUE, 2)));
        flightsDialog.add(scrollPane, BorderLayout.CENTER);

        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_BLUE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton closeButton = createStyledButton("Close", ACCENT_BLUE);
        closeButton.setPreferredSize(new Dimension(150, 40));
        closeButton.addActionListener(e -> flightsDialog.dispose());
        buttonPanel.add(closeButton);

        flightsDialog.add(buttonPanel, BorderLayout.SOUTH);

        flightsDialog.setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(350, 65));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DARK_BLUE, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JCheckBox createStyledCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setFont(new Font("Arial", Font.PLAIN, 16));
        checkBox.setBackground(BACKGROUND_BLUE);
        checkBox.setForeground(DARK_BLUE);
        checkBox.setFocusPainted(false);
        return checkBox;
    }

    private void showStyledMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.background", BACKGROUND_BLUE);
        UIManager.put("Panel.background", BACKGROUND_BLUE);
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private Passenger buildPassengerFromUserData(String[] userData) {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlightBookingGUI gui = new FlightBookingGUI();
            gui.setVisible(true);
        });
    }
}