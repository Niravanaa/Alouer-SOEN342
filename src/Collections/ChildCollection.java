public class ChildCollection {
    private List<Child> children;
    private int nextId = 1;

    public ChildrenCollection() {
        this.children = new ArrayList<Child>();
    }

    public List<Child> getAll() {
        return this.children;
    }

    public Child find(int id) {
        return this.children[id + 1];
    }

    public Child add(Child child) {
        child.setId(nextId);
        this.children.add(child);
        this.nextId++; 
    }
}