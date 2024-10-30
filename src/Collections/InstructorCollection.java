public class InstructorCollection {
    private List<Instructor> instructors;
    private int nextId = 1;

    public InstructorCollection() {
        this.instructors = new ArrayList<Instructor>();
    }

    public List<Instructor> getAll() {
        return this.instructors;
    }

    public Instructor find(int id) {
        return this.instructors[id + 1];
    }

    public Instructor add(Instructor instructor) {
        instructor.setId(nextId);
        this.instructors.add(lesson);
        this.nextId++; 
    }
}