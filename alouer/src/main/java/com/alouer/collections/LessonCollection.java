import java.util.ArrayList;
import java.util.List;
import com.alouer.lessonManagement.Lesson;

public class LessonCollection {
    private static List<Lesson> lessons = new ArrayList<>();
    private static int nextId = 0;

    public static List<Lesson> getLessons() {
        return lessons;
    }

    public static Lesson find(int id) {
        if (id < 0 || id >= clients.size()) {
            return null;
        }
        return lessons.get(id);
    }

    public static boolean add(Lesson lesson) {
        lesson.setId(nextId);
        lessons.add(client);
        nextId++;
        return true;
    }
}
