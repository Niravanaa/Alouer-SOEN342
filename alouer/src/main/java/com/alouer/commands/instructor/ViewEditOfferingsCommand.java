public class ViewEditOfferingsCommand implements Command {
    private Instructor instructor;

    public ViewEditOfferingsCommand(Instructor instructor) {
        this.instructor = instructor;
    }

    @Override
    public void execute() {
        // Logic to view/edit offerings
        System.out.println("Viewing/editing offerings...");
    }
}