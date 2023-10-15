package client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private final Connection connection;
    private final PreparedStatement insertClientPreparedSt;
    private final PreparedStatement getByIdPreparedSt;
    private final PreparedStatement getMaxIdPrepareSt;
    private final PreparedStatement setNamePrepareSt;
    private final PreparedStatement deleteClientPrepareSt;
    private final PreparedStatement getAllClientsPrepareSt;

    public ClientService(Connection connection) {
        this.connection = connection;

        try {
            insertClientPreparedSt = connection.prepareStatement("insert into client (name) values (?)");
            getByIdPreparedSt = connection.prepareStatement("select name from client where id = ?");
            getMaxIdPrepareSt = connection.prepareStatement("select max(id) from client");
            setNamePrepareSt = connection.prepareStatement("update client set name = ? where id = ?");
            deleteClientPrepareSt = connection.prepareStatement("delete from client where id = ?");
            getAllClientsPrepareSt = connection.prepareStatement("select * from client");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long create(String name) {
        try {
            validateClientName(name);
        } catch (ClientNameException e) {
            System.out.println(e.getMessage());
            return -1;
        }

        try {
            insertClientPreparedSt.setString(1, name);
            insertClientPreparedSt.executeUpdate();

            ResultSet resultSet = getMaxIdPrepareSt.executeQuery();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getById(long id) {
        try {
            validateClientId(id);
        } catch (ClientIdException e) {
            System.out.println(e.getMessage());
            return null;
        }

        try {
            getByIdPreparedSt.setLong(1, id);
            ResultSet resultSet = getByIdPreparedSt.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return resultSet.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setName(long id, String name) {
        try {
            validateClientId(id);
            validateClientName(name);
        } catch (ClientIdException | ClientNameException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            setNamePrepareSt.setString(1, name);
            setNamePrepareSt.setLong(2, id);
            setNamePrepareSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(long id) {
        try {
            validateClientId(id);
        } catch (ClientIdException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            deleteClientPrepareSt.setLong(1, id);
            deleteClientPrepareSt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Client> listAll() {
        List<Client> clients = new ArrayList<>();

        try {
            ResultSet resultSet = getAllClientsPrepareSt.executeQuery();
            while (resultSet.next()) {
                Client newClient = new Client();
                newClient.setId(resultSet.getLong(1));
                newClient.setName(resultSet.getString(2));
                clients.add(newClient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }

    public void closeResources() {
        try {
            connection.close();
            insertClientPreparedSt.close();
            getByIdPreparedSt.close();
            getMaxIdPrepareSt.close();
            setNamePrepareSt.close();
            deleteClientPrepareSt.close();
            getAllClientsPrepareSt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void validateClientId(long id) throws ClientIdException {
        if (id <= 0) {
            throw new ClientIdException();
        }
    }

    private void validateClientName(String name) throws ClientNameException {
        if (name.length() > 100 || name.length() < 2) {
            throw new ClientNameException();
        }
    }
}
