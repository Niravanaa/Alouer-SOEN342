import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    public static Map<String, Command> getCommands(UserType userType, Object user) {
        Map<String, Command> commands = new HashMap<>();
        
        if (userType == UserType.ADMIN) {
            commands.put("createLesson", new CreateLessonCommand((Administrator) user));
            commands.put("deleteUser", new DeleteUserCommand((Administrator) user));
            commands.put("logout", new LogoutCommand());
        } else if (userType == UserType.INSTRUCTOR) {
            commands.put("acceptOffering", new AcceptOfferingCommand((Instructor) user));
            commands.put("viewEditOfferings", new ViewEditOfferingsCommand((Instructor) user));
        } else if (userType == UserType.CLIENT) {
            commands.put("addDependent", new AddDependentCommand((Client) user));
            commands.put("createBooking", new CreateBookingCommand((Client) user));
            commands.put("viewEditBookings", new ViewEditBookingsCommand((Client) user));
        }
        
        return commands;
    }
}
