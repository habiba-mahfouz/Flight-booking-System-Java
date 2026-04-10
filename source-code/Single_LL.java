package dsproject;

import java.util.Arrays;

public class Single_LL {

    private Node head = null;
    private Node current = null;

    // Append node 
    public Boolean append(String[] data) {
        Boolean is_inserted = false;
        Node n = new Node(data);

        if (n != null) {
            is_inserted = true;
            if (head == null) {
                head = n;
            } else {
                current = head;
                while (current.getNext() != null) {
                    current = current.getNext();
                }
                current.setNext(n);
            }
        }
        return is_inserted;
    }

    // Insert node at position
    public Boolean insertATpos(String[] data, int pos) {
        Node n = new Node(data);
        Boolean is_position = false;
        current = head;
        if (n != null) {
            if (head != null) {
                is_position = true;
                if (pos == 0) {
                    n.setNext(head);
                    head = n;
                } else {
                    int index = 0;
                    while (current.getNext() != null && index < pos - 1) {
                        current = current.getNext();
                        index++;
                    }
                    if (current.getNext() != null) {
                        n.setNext(current.getNext());
                        current.setNext(n);
                    }
                }
            }
        }
        return is_position;
    }

    // Delete a node 
    public Boolean delete(String[] data) {
        Boolean is_deleted = false;
        current = head;
        if (head != null) {
            // first 
            if (Arrays.equals(head.getData(), data)) {
                current = head.getNext();
                head = current;
                is_deleted = true;
            } // middle and end 
            else {
                // Added check for current.getNext() != null to prevent crash if not found
                while (current.getNext() != null && !(Arrays.equals(current.getNext().getData(), data))) {
                    current = current.getNext();
                }

                if (current.getNext() != null) {
                    current.setNext(current.getNext().getNext());
                    is_deleted = true;
                }
            }
        }

        return is_deleted;
    }

    // display all nodes (prints first element only)
    public void display() {
        if (head != null) {
            current = head;
            while (current.getNext() != null) {
                System.out.print(current.getData()[0] + " -> "); // Printing first field for clarity
                current = current.getNext();
            }
            System.out.println(current.getData()[0] + " -> null");
        } else {
            System.out.println("No nodes yet.");
        }
    }

    // Display all flights assuming data format: [from, to, seatClass, price]
    public void displayFlights() {
        if (head == null) {
            System.out.println("No previous flights.");
            return;
        }
        Node temp = head;
        int index = 1;
        System.out.println("\n--- Your Previous Flights ---");
        while (temp != null) {
            String[] d = temp.getData();
            String from = d.length > 0 ? d[0] : "";
            String to = d.length > 1 ? d[1] : "";
            String seatClass = d.length > 2 ? d[2] : "";
            String price = d.length > 3 ? d[3] : "";
            System.out.println("Flight " + index + ": From " + from + " To " + to +
                    ", Class: " + seatClass + ", Price: " + price);
            temp = temp.getNext();
            index++;
        }
    }

    // Search for user for log in   
    public int LoginSearch(String[] data) {
        int found_identifier = 0;
        int found_password = 0;

        current = head;
        while (current != null) {
            String[] UserData = current.getData();
            Boolean identifier = data[0].equals(UserData[0])
                    || data[0].equals(UserData[1]) || data[0].equals(UserData[2]);
            Boolean password = data[1].equals(UserData[3]);

            if (identifier && password) {
                found_identifier = 1;
                found_password = 1;
                break;
            } else if (identifier && !password) {
                found_identifier = 1;
                break;
            } else if (!identifier && password) {
                found_password = 1;
                break;
            }
            current = current.getNext();
        }

        return (found_password + found_identifier);
    }

    // search before sign up 
    public int SignupSearch(String[] data) {

        int is_exist = 0;
        current = head;
        while (current != null) {
            String[] UserData = current.getData();
            if (data[0].equals(UserData[0])) {
                // user name exists 
                is_exist = -2;
                break;
            }
            current = current.getNext();
        }

        current = head;
        while (current != null) {
            String[] UserData = current.getData();
            if (data[0].equals(UserData[0]) && data[1].equals(UserData[1])
                    && data[2].equals(UserData[2]) && data[3].equals(UserData[3])) {
                // user account exists 
                is_exist = 2;
                break;
            }
            current = current.getNext();
        }

        return is_exist;
    }

    // Find user by identifier (name / email / phone)
    public String[] findUser(String identifier) {
        Node temp = head;
        while (temp != null) {
            String[] userData = temp.getData();
            boolean identifierMatch =
                    identifier.equals(userData[0]) ||  // name
                    identifier.equals(userData[1]) ||  // email
                    identifier.equals(userData[2]);    // phone

            if (identifierMatch) {
                return userData;
            }
            temp = temp.getNext();
        }
        return null;
    }
}