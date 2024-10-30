public class BookingCollection {
    private List<Booking> bookings;
    private int nextId = 1;

    public BookingCollection() {
        this.bookings = new ArrayList<Booking>();
    }

    public List<Booking> getAll() {
        return this.bookings;
    }

    public Booking find(int id) {
        return this.bookings[id + 1];
    }

    public Booking add(Booking booking) {
        booking.setId(nextId);
        this.bookings.add(child);
        this.nextId++; 
    }
}