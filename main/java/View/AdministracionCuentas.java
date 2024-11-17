/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author PC
 */
import DataBaseUntil.DataBaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdministracionCuentas extends JInternalFrame {
    private JTable tablaCuentas;
    private JTextField txtTipoTransaccion, txtMonto, txtDescripcion;
    private JButton btnAgregarCuenta, btnRealizarTransaccion;
    private int clienteIDSeleccionado;

    public AdministracionCuentas(int clienteID) {
        this.clienteIDSeleccionado = clienteID;

        setTitle("Administración de Cuentas - Cliente ID: " + clienteID);
        setSize(1320, 750);
        setLayout(new BorderLayout());

        // Panel superior: Información de la cuenta
        JPanel panelCuenta = new JPanel(new BorderLayout());
        panelCuenta.setBackground(new Color(45, 52, 54));

        JLabel lblBuscarCuentas = new JLabel("Cuentas del Cliente:");
        lblBuscarCuentas.setForeground(Color.WHITE);
        panelCuenta.add(lblBuscarCuentas, BorderLayout.WEST);

        // Panel central: Tabla de cuentas
        tablaCuentas = new JTable(new DefaultTableModel(new Object[]{"Cuenta ID", "Tipo Transacción", "Monto", "Fecha", "Descripción"}, 0));
        tablaCuentas.setRowHeight(25);
        tablaCuentas.setBackground(new Color(223, 230, 233));
        JScrollPane scrollTablaCuentas = new JScrollPane(tablaCuentas);

        // Panel inferior: Formulario de cuenta y botones
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        panelFormulario.setBackground(new Color(45, 52, 54));

        JLabel lblTipoTransaccion = new JLabel("Tipo Transacción:");
        lblTipoTransaccion.setForeground(Color.WHITE);
        txtTipoTransaccion = new JTextField();

        JLabel lblMonto = new JLabel("Monto:");
        lblMonto.setForeground(Color.WHITE);
        txtMonto = new JTextField();

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setForeground(Color.WHITE);
        txtDescripcion = new JTextField();

        btnAgregarCuenta = new JButton("Agregar Cuenta");
        btnAgregarCuenta.setBackground(new Color(85, 239, 196));
        btnAgregarCuenta.setForeground(Color.BLACK);
        btnAgregarCuenta.addActionListener(e -> agregarCuenta());

        btnRealizarTransaccion = new JButton("Realizar Transacción");
        btnRealizarTransaccion.setBackground(new Color(255, 159, 67));
        btnRealizarTransaccion.setForeground(Color.BLACK);
        btnRealizarTransaccion.addActionListener(e -> realizarTransaccion());

        panelFormulario.add(lblTipoTransaccion);
        panelFormulario.add(txtTipoTransaccion);
        panelFormulario.add(lblMonto);
        panelFormulario.add(txtMonto);
        panelFormulario.add(lblDescripcion);
        panelFormulario.add(txtDescripcion);
        panelFormulario.add(btnAgregarCuenta);
        panelFormulario.add(btnRealizarTransaccion);

        // Agregar componentes al frame
        add(panelCuenta, BorderLayout.NORTH);
        add(scrollTablaCuentas, BorderLayout.CENTER);
        add(panelFormulario, BorderLayout.SOUTH);

        // Cargar todas las cuentas del cliente seleccionado
        cargarCuentasCliente();
    }

    private void cargarCuentasCliente() {
        DefaultTableModel modelo = (DefaultTableModel) tablaCuentas.getModel();
        modelo.setRowCount(0);

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT cuenta_id, tipo_transaccion, monto, fecha, descripcion FROM cuentas WHERE cliente_id = ?")) {

            stmt.setInt(1, clienteIDSeleccionado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("cuenta_id"),
                        rs.getString("tipo_transaccion"),
                        rs.getDouble("monto"),
                        rs.getDate("fecha"),
                        rs.getString("descripcion")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar cuentas: " + e.getMessage());
        }
    }

    private void agregarCuenta() {
        String tipoTransaccion = txtTipoTransaccion.getText();
        double monto = Double.parseDouble(txtMonto.getText());
        String descripcion = txtDescripcion.getText();

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO cuentas (cliente_id, tipo_transaccion, monto, descripcion) VALUES (?, ?, ?, ?)")) {

            stmt.setInt(1, clienteIDSeleccionado);
            stmt.setString(2, tipoTransaccion);
            stmt.setDouble(3, monto);
            stmt.setString(4, descripcion);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Cuenta agregada exitosamente.");
                cargarCuentasCliente();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar cuenta: " + e.getMessage());
        }
    }

    private void realizarTransaccion() {
        int filaSeleccionada = tablaCuentas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cuenta para realizar la transacción.");
            return;
        }

        int cuentaID = (int) tablaCuentas.getValueAt(filaSeleccionada, 0);
        String tipoTransaccion = txtTipoTransaccion.getText();
        double monto = Double.parseDouble(txtMonto.getText());

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT monto FROM cuentas WHERE cuenta_id = ?")) {

            stmt.setInt(1, cuentaID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double saldoActual = rs.getDouble("monto");

                // Realizar la transacción según el tipo
                if (tipoTransaccion.equalsIgnoreCase("deposito")) {
                    saldoActual += monto;
                } else if (tipoTransaccion.equalsIgnoreCase("retiro")) {
                    if (saldoActual >= monto) {
                        saldoActual -= monto;
                    } else {
                        JOptionPane.showMessageDialog(this, "No hay suficiente saldo.");
                        return;
                    }
                }

                // Actualizar el monto de la cuenta
                try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE cuentas SET monto = ? WHERE cuenta_id = ?")) {
                    updateStmt.setDouble(1, saldoActual);
                    updateStmt.setInt(2, cuentaID);
                    int filas = updateStmt.executeUpdate();

                    if (filas > 0) {
                        JOptionPane.showMessageDialog(this, "Transacción realizada exitosamente.");
                        cargarCuentasCliente();
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al realizar transacción: " + e.getMessage());
        }
    }
}
