public class LocationCollection {
    private List<Location> locations;
    private int nextId = 1;

    public LocationCollection() {
        this.locations = new ArrayList<Location>();
    }

    public List<Location> getAll() {
        return this.locations;
    }

    public Location find(int id) {
        return this.locations[id + 1];
    }

    public Location add(Location location) {
        location.setId(nextId);
        this.locations.add(lesson);
        this.nextId++; 
    }
}