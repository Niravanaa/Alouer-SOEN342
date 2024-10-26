public class Administrator extends Client {
    private static Administrator singleton = null;

    // Does admin even log in?
    public Administrator(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
        super.role = null; // An admin is neither a client nor an instructor
    }

    // TODO implement singleton pattern
}
