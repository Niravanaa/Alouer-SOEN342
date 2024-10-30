import java.util.ArrayList;
import java.util.List;
import com.alouer.models.child;

public class ChildCollection {
    private static List<Child> children = new ArrayList<>();
    private static int nextId = 0;

    public static List<Child> getChildren() {
        return bookings;
    }

    public static Child find(int id) {
        if (id < 1 || id > children.size()) {
            return null;
        }
        return bookings.get(id - 1);
    }

    public static boolean add(Child child) {
        child.setId(nextId);
        children.add(child);
        nextId++;
        return true;
    }
}
