import java.util.ArrayList;

public class Instructor extends Client {
    private ArrayList<Lesson> lessons;

    public Instructor(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
        super.role = UserType.INSTRUCTOR;
        lessons = new ArrayList<Lesson>();
    }

    // TODO method to add Lesson

    // TODO method to remove Lesson

    // TODO method to update availability
}
