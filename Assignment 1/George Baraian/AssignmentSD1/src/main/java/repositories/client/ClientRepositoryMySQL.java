package repositories.client;

import model.Client;
import model.builder.ClientBuilder;

import java.sql.*;
import java.util.List;

public class ClientRepositoryMySQL implements ClientRepository{

    private final Connection connection;
    private final ClientRepository clientRepository;

    public ClientRepositoryMySQL(Connection connection, ClientRepository clientRepository) {
        this.connection = connection;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findAll() {
        return null;
    }

    @Override
    public Client findById(Long id) {
        try{
            Statement statement = connection.createStatement();
            String fetchClientSql = "SELECT * from client where id=" + id;
            ResultSet resultSet = statement.executeQuery(fetchClientSql);
            if(resultSet.next()){
                Client client = new ClientBuilder()
                        .setId(resultSet.getLong("id"))
                        .setName(resultSet.getString("name"))
                        .setPersonalNumericalCode(resultSet.getLong("personalNumericalCode"))
                        .build();
                Client client1 = client;
                return client1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateName(String updatedName, Long id) {
        try{
            String updateNameSql = "UPDATE client SET name = ? WHERE id=" + id;
            PreparedStatement preparedStatement = connection.prepareStatement(updateNameSql);
            preparedStatement.setString(1,updatedName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePersonalNumericalCode(Long updatedPersonalNumericalCode, Long id) {
        try {
            String updateNameSql = "UPDATE client SET personalNumericalCode = ? WHERE id=" + id;
            PreparedStatement preparedStatement = connection.prepareStatement(updateNameSql);
            preparedStatement.setLong(1, updatedPersonalNumericalCode);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAddress(String updatedAddress, Long id) {
        try {
            String updateNameSql = "UPDATE client SET address = ? WHERE id=" + id;
            PreparedStatement preparedStatement = connection.prepareStatement(updateNameSql);
            preparedStatement.setString(1, updatedAddress);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean save(Client client) {
        try{
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO client values (null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,client.getName());
            preparedStatement.setString(2,client.getAddress());
            preparedStatement.setLong(3, client.getPersonalNumericalCode());
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            long clientId = rs.getLong(1);
            client.setId(clientId);

            clientRepository.save(client);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void removeAll() {
        try{
            Statement statement = connection.createStatement();
            String sql = "DELETE from client where id>= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
