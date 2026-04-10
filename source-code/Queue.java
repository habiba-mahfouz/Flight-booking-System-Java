package dsproject;

public class Queue {
    private Node Front, Rear;
    private int size;  // current number of elements in the queue

    public Queue() {
        Front = Rear = null;
        size = 0;
    }
    
    // Add a new node at the end of the queue
    public boolean EnQueue(String[] data) {
        boolean retval = false;
        Node node = new Node(data);
        
        if (node != null) {
            if (Rear == null) {  // queue is empty   
                Rear = Front = node;    
            } else { // queue has value
                Rear.setNext(node);
                Rear = node;    
            }
            size++;   // one element added
            retval = true;
        }
        return retval;
    }
    
    // Remove a node from the front of the queue
    public Node DeQueue() {
        Node retval = Front;
        if (Front != null) { // there is a queue
            Front = Front.getNext();
            size--;           // one element removed
            if (Front == null) {
                Rear = null;   
            }
        }
        return retval;
    }
    
    // Check if the queue is empty
    public boolean isEmpty() {
        return Front == null && Rear == null;    
    }

    // Get current number of elements (seats) in the queue
    public int getSize() {
        return size;
    }
    
    // Display all elements in the queue
    public void display() {
        if (isEmpty()) {   
            System.out.println("Empty Queue"); 
            return;
        }
        Node temp = Front;
        System.out.print("Queue: ");
        while (temp != null) {
            System.out.print(temp.getData()[0] + " -> ");  // Print first value in data (e.g., seat label)
            temp = temp.getNext();
        }
        System.out.println("null");
    }
    
    // Search by first value in data
    public boolean search(String value) {
        if (isEmpty()) {   
            return false;   
        }
        Node temp = Front;
        while (temp != null) {
            if (temp.getData()[0].equals(value)) {  // Search by name/label
                return true;   
            }
            temp = temp.getNext();
        }
        return false;
    }
    
    public boolean update(String oldValue, String[] newValue) {
        Node temp = Front;
        while (temp != null) {
            if (temp.getData()[0].equals(oldValue)) {
                // Note: Need setter in Node class to replace the whole data array
                return true;
            }
            temp = temp.getNext();  
        }
        return false;    
    }
}