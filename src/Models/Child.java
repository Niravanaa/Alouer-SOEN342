import java.time.*;
import java.util.Random;

public class Child {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private int parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isConnected;

    public Child(String firstName, String lastName, LocalDate dateOfBirth, int parentId) {
        id = new Random().nextInt(99999999);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.parentId = parentId;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        isConnected = false; // set to true when connecting to terminal
    }
}
