public class AcceptOfferingCommand implements Command {
    private Instructor instructor;

    public AcceptOfferingCommand(Instructor instructor) {
        this.instructor = instructor;
    }

    @Override
    public void execute() {
        // Logic to accept an offering
        System.out.println("Accepting an offering...");
    }
}