package client;

import org.junit.jupiter.api.*;
import storage.DatabaseInitService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

class ClientServiceTest {
    private ClientService clientService;

    @BeforeEach
    public void beforeEach() throws SQLException {
        String connectionUrl = "jdbc:h2:mem:megasoftDB;DB_CLOSE_DELAY=-1;MODE=MySQL;DATABASE_TO_LOWER=TRUE";
        new DatabaseInitService().initDbH2(connectionUrl);
        Connection connection = DriverManager.getConnection(connectionUrl);
        clientService = new ClientService(connection);
    }

    @AfterEach
    public void afterEach() {
        clientService.closeResources();
    }

    @Test
    void testCreateAndGetById() {
        String expectedName = "test";
        long id = clientService.create(expectedName);

        Assertions.assertEquals(expectedName, clientService.getById(id));
    }

    @Test
    void testThatCreateHandleTooShotNames() {
        Assertions.assertEquals(-1, clientService.create("n"));
    }

    @Test
    void testThatCreateHandleTooLongNames() {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            name.append(i);
        }

        Assertions.assertEquals(-1, clientService.create(name.toString()));
    }

    @Test
    void testThatGetByIdHandleIncorrectId() {
        Assertions.assertNull(clientService.getById(0));
    }


    @Test
    void testSetName() {
        long id = clientService.create("old name");

        String newName = "new name";
        clientService.setName(id, newName);

        Assertions.assertEquals(newName, clientService.getById(id));
    }

    @Test
    void testDeleteById() {
        long id = clientService.create("test");
        clientService.deleteById(id);

        Assertions.assertNull(clientService.getById(id));
    }

    @Test
    @Disabled
    void testListAll() {
        String testName = "test";
        long id = clientService.create("testName");

        Client expectedClient = new Client();
        expectedClient.setId(id);
        expectedClient.setName(testName);

        List<Client> expectedClients = Collections.singletonList(expectedClient);
        List<Client> actualClients = clientService.listAll();

        Assertions.assertEquals(expectedClients, actualClients);
    }
}