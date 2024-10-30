import java.util.ArrayList;
import java.util.List;
import com.alouer.lessonManagement.booking;

public class BookingCollection {
    private static List<Booking> bookings = new ArrayList<>();
    private static int nextId = 0;

    public static List<Booking> getBookings() {
        return bookings;
    }

    public static Booking find(int id) {
        if (id < 1 || id > bookings.size()) {
            return null;
        }
        return bookings.get(id - 1);
    }

    public static boolean add(Booking booking) {
        booking.setId(nextId);
        bookings.add(booking);
        nextId++;
        return true;
    }
}
