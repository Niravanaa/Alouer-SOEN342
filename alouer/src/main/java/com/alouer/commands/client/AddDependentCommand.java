public class AddDependentCommand implements Command {
    private Client client;

    public AddDependentCommand(Client client) {
        this.client = client;
    }

    @Override
    public void execute() {
        // Logic to add an underage dependent
        System.out.println("Adding an underage dependent...");
    }
}