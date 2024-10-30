public class Administrator {
    private String firstName = "Admin";
    private String lastName = "User";
    private String email = "admin@example.com";
    private String password = "password123";

    private static Administrator instance = null;

    private Administrator() {
        // Constructor remains empty; values are initialized directly
    }

    public static Administrator getInstance() {
        if (instance == null) {
            instance = new Administrator();
        }
        return instance;
    }

    // Getters for the fields
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
}
