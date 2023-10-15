import client.ClientService;
import storage.DatabaseInitService;
import storage.DatabaseMysql;

public class App {
    public static void main(String[] args) {
        new DatabaseInitService().initDbMysql();

        ClientService clientService = new ClientService(DatabaseMysql.getInstance().getConnection());

        System.out.println(clientService.create("new client"));
        clientService.setName(5, "new");
        System.out.println(clientService.getById(4));
        clientService.deleteById(6);
        clientService.listAll().forEach(System.out::println);

        clientService.closeResources();
    }
}
