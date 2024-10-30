public class LessonCollection {
    private List<Lesson> lessons;
    private int nextId = 1;

    public LessonCollection() {
        this.lessons = new ArrayList<Lesson>();
    }

    public List<Lesson> getAll() {
        return this.lessons;
    }

    public Lesson find(int id) {
        return this.lessons[id + 1];
    }

    public Lesson add(Lesson lesson) {
        lesson.setId(nextId);
        this.lessons.add(lesson);
        this.nextId++; 
    }
}