import java.time.*;
import java.util.ArrayList;
import java.util.Random;

public class Client {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    protected UserType role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isConnected;
    private ArrayList<Child> children;
    private ArrayList<Booking> bookings;

    public Client(String firstName, String lastName, String email, String password) {
        id = new Random().nextInt(99999999);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password; // TODO hash password
        role = UserType.CLIENT;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        isConnected = false; // set to true when connecting to terminal
        children = new ArrayList<Child>();
        bookings = new ArrayList<Booking>();
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // TODO hash password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getRole() {
        return role;
    }

    // Can a user change their role?
    public void setRole(UserType role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }
}
