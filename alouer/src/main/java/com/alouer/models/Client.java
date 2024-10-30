import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Client {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserType role;
    private Date createdAt;
    private Date updatedAt;
    private boolean isConnected;
    private List<Integer> children;
    private List<Integer> bookings;

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = UserType.CLIENT;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.isConnected = false;
        this.children = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getRole() {
        return role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public List<Integer> getChildren() {
        return children;
    }

    public List<Integer> getBookings() {
        return bookings;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addChild(int childId) {
        this.children.add(childId);
    }

    public void addBooking(int bookingId) {
        this.bookings.add(bookingId);
    }
}
