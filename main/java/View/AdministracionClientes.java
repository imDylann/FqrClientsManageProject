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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdministracionClientes extends JInternalFrame {
    private JTable tablaClientes;
    private JTextField txtNombre, txtDireccion, txtTelefono, txtBuscarID;
    private JButton btnAgregar, btnModificar, btnVerCuenta;

    public AdministracionClientes() {
        setTitle("Administración de Clientes");
        setSize(1320, 750); // Tamaño ajustado
        setLayout(new BorderLayout());

        // Panel superior: Búsqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout());
        panelBusqueda.setBackground(new Color(45, 52, 54));
        JLabel lblBuscar = new JLabel("Buscar por ID:");
        lblBuscar.setForeground(Color.WHITE);
        txtBuscarID = new JTextField();
        txtBuscarID.setBackground(new Color(200, 200, 200));
        txtBuscarID.setForeground(Color.BLACK);
        txtBuscarID.setBorder(BorderFactory.createLineBorder(new Color(45, 52, 54), 2));
        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(txtBuscarID, BorderLayout.CENTER);

        // Botón para buscar
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(85, 239, 196));
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.addActionListener(e -> {
            if (txtBuscarID.getText().isEmpty()) {
                cargarTodosLosClientes(); // Mostrar todos los clientes si la barra está vacía
            } else {
                cargarClientePorID();
            }
        });
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        // Panel central: Tabla
        tablaClientes = new JTable(new DefaultTableModel(new Object[]{"ID", "Nombre", "Dirección", "Teléfono"}, 0));
        tablaClientes.setRowHeight(25);
        tablaClientes.setBackground(new Color(223, 230, 233));
        JScrollPane scrollTabla = new JScrollPane(tablaClientes);
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarDatosSeleccionados();
            }
        });

        // Panel inferior: Formulario y botones
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10)); // Espacio extra para botón "Ver Cuenta"
        panelFormulario.setBackground(new Color(45, 52, 54));

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(Color.WHITE);
        txtNombre = new JTextField();
        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setForeground(Color.WHITE);
        txtDireccion = new JTextField();
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setForeground(Color.WHITE);
        txtTelefono = new JTextField();

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(85, 239, 196));
        btnAgregar.setForeground(Color.BLACK);
        btnAgregar.addActionListener(e -> agregarCliente());

        btnModificar = new JButton("Modificar");
        btnModificar.setBackground(new Color(255, 159, 67));
        btnModificar.setForeground(Color.BLACK);
        btnModificar.addActionListener(e -> modificarCliente());

        btnVerCuenta = new JButton("Ver Cuenta"); // Nuevo botón
        btnVerCuenta.setBackground(new Color(129, 236, 236));
        btnVerCuenta.setForeground(Color.BLACK);
        btnVerCuenta.addActionListener(e -> verCuentaCliente());

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblDireccion);
        panelFormulario.add(txtDireccion);
        panelFormulario.add(lblTelefono);
        panelFormulario.add(txtTelefono);
        panelFormulario.add(btnAgregar);
        panelFormulario.add(btnModificar);
        panelFormulario.add(new JLabel()); // Espacio vacío para alineación
        panelFormulario.add(btnVerCuenta); // Agregar botón "Ver Cuenta"

        // Agregar componentes al frame
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelFormulario, BorderLayout.SOUTH);

        // Cargar datos iniciales
        cargarTodosLosClientes();
    }

    private void cargarTodosLosClientes() {
        DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
        modelo.setRowCount(0);

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT cliente_id, nombre, direccion, telefono FROM clientes")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("cliente_id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + e.getMessage());
        }
    }

    private void cargarClientePorID() {
        String id = txtBuscarID.getText();

        DefaultTableModel modelo = (DefaultTableModel) tablaClientes.getModel();
        modelo.setRowCount(0);

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT cliente_id, nombre, direccion, telefono FROM clientes WHERE cliente_id = ?")) {

            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("cliente_id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono")
                });
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún cliente con el ID proporcionado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar cliente: " + e.getMessage());
        }
    }

    private void cargarDatosSeleccionados() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada != -1) {
            txtNombre.setText(tablaClientes.getValueAt(filaSeleccionada, 1).toString());
            txtDireccion.setText(tablaClientes.getValueAt(filaSeleccionada, 2).toString());
            txtTelefono.setText(tablaClientes.getValueAt(filaSeleccionada, 3).toString());
        }
    }

    private void agregarCliente() {
        String nombre = txtNombre.getText();
        String direccion = txtDireccion.getText();
        String telefono = txtTelefono.getText();

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO clientes (nombre, direccion, telefono) VALUES (?, ?, ?)")) {

            stmt.setString(1, nombre);
            stmt.setString(2, direccion);
            stmt.setString(3, telefono);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente.");
                cargarTodosLosClientes();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar cliente: " + e.getMessage());
        }
    }

    private void modificarCliente() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para modificar.");
            return;
        }

        int clienteID = (int) tablaClientes.getValueAt(filaSeleccionada, 0);
        String nombre = txtNombre.getText();
        String direccion = txtDireccion.getText();
        String telefono = txtTelefono.getText();

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE clientes SET nombre = ?, direccion = ?, telefono = ? WHERE cliente_id = ?")) {

            stmt.setString(1, nombre);
            stmt.setString(2, direccion);
            stmt.setString(3, telefono);
            stmt.setInt(4, clienteID);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Cliente modificado exitosamente.");
                cargarTodosLosClientes();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar cliente: " + e.getMessage());
        }
    }

    private void verCuentaCliente() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para ver su cuenta.");
            return;
        }

        int clienteID = (int) tablaClientes.getValueAt(filaSeleccionada, 0);

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cuentas WHERE cliente_id = ?")) {

            stmt.setInt(1, clienteID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String detallesCuenta = String.format("ID Cuenta: %d\nmonto: %.2f\ntipo_transaccion: %s",
                        rs.getInt("cuenta_id"),
                        rs.getDouble("monto"),
                        rs.getString("tipo_transaccion"));

                JOptionPane.showMessageDialog(this, detallesCuenta, "Detalles de la Cuenta", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "El cliente no tiene una cuenta asociada.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar detalles de la cuenta: " + e.getMessage());
        }
    }
}


