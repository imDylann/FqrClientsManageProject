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
import Model.Cuenta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO {
    private static final String INSERT_CUENTA_SQL = "INSERT INTO Cuentas (cliente_id, tipo_transaccion, monto, descripcion, fecha) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_CUENTAS_BY_CLIENTE = "SELECT * FROM Cuentas WHERE cliente_id = ?";
    private static final String UPDATE_CUENTA_SQL = "UPDATE Cuentas SET tipo_transaccion = ?, monto = ?, descripcion = ?, fecha = ? WHERE id = ?";
    private static final String DELETE_CUENTA_SQL = "DELETE FROM Cuentas WHERE id = ?";

    // Método para insertar una nueva cuenta
    public void insertarCuenta(Cuenta cuenta) throws SQLException {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CUENTA_SQL)) {
            preparedStatement.setInt(1, cuenta.getClienteId());
            preparedStatement.setString(2, cuenta.getTipoTransaccion());
            preparedStatement.setDouble(3, cuenta.getMonto());
            preparedStatement.setString(4, cuenta.getDescripcion());
            preparedStatement.setDate(5, new java.sql.Date(cuenta.getFecha().getTime()));
            preparedStatement.executeUpdate();
        }
    }

    // Método para obtener todas las cuentas de un cliente
    public List<Cuenta> obtenerCuentasPorCliente(int clienteId) throws SQLException {
        List<Cuenta> cuentas = new ArrayList<>();
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CUENTAS_BY_CLIENTE)) {
            preparedStatement.setInt(1, clienteId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String tipoTransaccion = resultSet.getString("tipo_transaccion");
                double monto = resultSet.getDouble("monto");
                String descripcion = resultSet.getString("descripcion");
                java.util.Date fecha = resultSet.getDate("fecha");
                cuentas.add(new Cuenta(id, clienteId, tipoTransaccion, monto, descripcion, fecha));
            }
        }
        return cuentas;
    }

    // Método para actualizar una cuenta
    public void actualizarCuenta(Cuenta cuenta) throws SQLException {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CUENTA_SQL)) {
            preparedStatement.setString(1, cuenta.getTipoTransaccion());
            preparedStatement.setDouble(2, cuenta.getMonto());
            preparedStatement.setString(3, cuenta.getDescripcion());
            preparedStatement.setDate(4, new java.sql.Date(cuenta.getFecha().getTime()));
            preparedStatement.setInt(5, cuenta.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Método para eliminar una cuenta
    public void eliminarCuenta(int cuentaId) throws SQLException {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CUENTA_SQL)) {
            preparedStatement.setInt(1, cuentaId);
            preparedStatement.executeUpdate();
        }
    }
}
