public class ClientCollection {
    private List<Client> clients;
    private int nextId = 1;

    public ClientCollection() {
        this.clients = new ArrayList<Client>();
    }

    public List<Client> getAll() {
        return this.clients;
    }

    public Client find(int id) {
        return this.clients[id + 1];
    }

    public Client add(Client client) {
        client.setId(nextId);
        this.clients.add(client);
        this.nextId++; 
    }
}