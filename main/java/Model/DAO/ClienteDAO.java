/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DAO;

/**
 *
 * @author PC
 */
import DataBaseUntil.DataBaseConnection;
import Model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private static final String INSERT_CLIENTE_SQL = "INSERT INTO Clientes (nombre, direccion, telefono) VALUES (?, ?, ?)";
    private static final String SELECT_CLIENTE_BY_ID = "SELECT * FROM Clientes WHERE id = ?";
    private static final String SELECT_ALL_CLIENTES = "SELECT * FROM Clientes";
    private static final String UPDATE_CLIENTE_SQL = "UPDATE Clientes SET nombre = ?, direccion = ?, telefono = ? WHERE id = ?";
    private static final String DELETE_CLIENTE_SQL = "DELETE FROM Clientes WHERE id = ?";

    // Método para insertar un nuevo cliente
    public void insertarCliente(Cliente cliente) throws SQLException {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CLIENTE_SQL)) {
            preparedStatement.setString(1, cliente.getNombre());
            preparedStatement.setString(2, cliente.getDireccion());
            preparedStatement.setString(3, cliente.getTelefono());
            preparedStatement.executeUpdate();
        }
    }

    // Método para obtener un cliente por ID
    public Cliente obtenerClientePorId(int id) throws SQLException {
        Cliente cliente = null;
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CLIENTE_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String direccion = resultSet.getString("direccion");
                String telefono = resultSet.getString("telefono");
                cliente = new Cliente(id, nombre, direccion, telefono);
            }
        }
        return cliente;
    }

    // Método para obtener todos los clientes
    public List<Cliente> obtenerTodosLosClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CLIENTES);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String direccion = resultSet.getString("direccion");
                String telefono = resultSet.getString("telefono");
                clientes.add(new Cliente(id, nombre, direccion, telefono));
            }
        }
        return clientes;
    }

    // Método para actualizar un cliente
    public void actualizarCliente(Cliente cliente) throws SQLException {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLIENTE_SQL)) {
            preparedStatement.setString(1, cliente.getNombre());
            preparedStatement.setString(2, cliente.getDireccion());
            preparedStatement.setString(3, cliente.getTelefono());
            preparedStatement.setInt(4, cliente.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Método para eliminar un cliente
    public void eliminarCliente(int id) throws SQLException {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CLIENTE_SQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
