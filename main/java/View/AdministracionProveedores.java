/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author PC
 */
package View;

import DataBaseUntil.DataBaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class AdministracionProveedores extends JInternalFrame {
    private JTable tablaProveedores;
    private JTextField txtNombre, txtDireccion, txtTelefono, txtBuscarID, txtEmail, txtRepresentante;
    private JButton btnAgregar, btnModificar, btnVerDetalles;

    public AdministracionProveedores() {
        setTitle("Administración de Proveedores");
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
                cargarTodosLosProveedores(); // Mostrar todos los proveedores si la barra está vacía
            } else {
                cargarProveedorPorID();
            }
        });
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        // Panel central: Tabla
        tablaProveedores = new JTable(new DefaultTableModel(new Object[]{"ID", "Nombre", "Dirección", "Teléfono", "Email", "Representante"}, 0));
        tablaProveedores.setRowHeight(25);
        tablaProveedores.setBackground(new Color(223, 230, 233));
        JScrollPane scrollTabla = new JScrollPane(tablaProveedores);
        tablaProveedores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarDatosSeleccionados();
            }
        });

        // Panel inferior: Formulario y botones
        JPanel panelFormulario = new JPanel();
        GroupLayout layout = new GroupLayout(panelFormulario);
        panelFormulario.setLayout(layout);

        // Definir el layout para el formulario
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(Color.BLACK);
        txtNombre = new JTextField(20);  // Establecer tamaño preferido
        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setForeground(Color.BLACK);
        txtDireccion = new JTextField(20);  // Establecer tamaño preferido
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setForeground(Color.BLACK);
        txtTelefono = new JTextField(20);  // Establecer tamaño preferido
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.BLACK);
        txtEmail = new JTextField(20);  // Establecer tamaño preferido
        JLabel lblRepresentante = new JLabel("Representante:");
        lblRepresentante.setForeground(Color.BLACK);
        txtRepresentante = new JTextField(20);  // Establecer tamaño preferido

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(85, 239, 196));
        btnAgregar.setForeground(Color.BLACK);
        btnAgregar.addActionListener(e -> agregarProveedor());

        btnModificar = new JButton("Modificar");
        btnModificar.setBackground(new Color(255, 159, 67));
        btnModificar.setForeground(Color.BLACK);
        btnModificar.addActionListener(e -> modificarProveedor());

        btnVerDetalles = new JButton("Ver Detalles");
        btnVerDetalles.setBackground(new Color(129, 236, 236));
        btnVerDetalles.setForeground(Color.BLACK);
        btnVerDetalles.addActionListener(e -> verDetallesProveedor());

        // Definir el orden de los componentes en el GroupLayout
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(lblNombre)
                .addComponent(lblDireccion)
                .addComponent(lblTelefono)
                .addComponent(lblEmail)
                .addComponent(lblRepresentante)
                .addComponent(btnAgregar)
            )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(txtNombre)
                .addComponent(txtDireccion)
                .addComponent(txtTelefono)
                .addComponent(txtEmail)
                .addComponent(txtRepresentante)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(btnModificar)
                    .addComponent(btnVerDetalles)
                )
            )
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblNombre)
                .addComponent(txtNombre)
            )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblDireccion)
                .addComponent(txtDireccion)
            )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblTelefono)
                .addComponent(txtTelefono)
            )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblEmail)
                .addComponent(txtEmail)
            )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblRepresentante)
                .addComponent(txtRepresentante)
            )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(btnAgregar)
                .addComponent(btnModificar)
                .addComponent(btnVerDetalles)
            )
        );

        // Agregar componentes al frame
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelFormulario, BorderLayout.SOUTH);

        // Cargar datos iniciales
        cargarTodosLosProveedores();
    }

    private void cargarTodosLosProveedores() {
        DefaultTableModel modelo = (DefaultTableModel) tablaProveedores.getModel();
        modelo.setRowCount(0);

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT proveedor_id, nombre, direccion, telefono, email, representante FROM proveedores")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("proveedor_id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("representante")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage());
        }
    }

    private void cargarProveedorPorID() {
        String id = txtBuscarID.getText();

        DefaultTableModel modelo = (DefaultTableModel) tablaProveedores.getModel();
        modelo.setRowCount(0);

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT proveedor_id, nombre, direccion, telefono, email, representante FROM proveedores WHERE proveedor_id = ?")) {

            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("proveedor_id"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("representante")
                });
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún proveedor con el ID proporcionado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar proveedor: " + e.getMessage());
        }
    }

    private void cargarDatosSeleccionados() {
        int filaSeleccionada = tablaProveedores.getSelectedRow();
        if (filaSeleccionada != -1) {
            txtNombre.setText(tablaProveedores.getValueAt(filaSeleccionada, 1).toString());
            txtDireccion.setText(tablaProveedores.getValueAt(filaSeleccionada, 2).toString());
            txtTelefono.setText(tablaProveedores.getValueAt(filaSeleccionada, 3).toString());
            txtEmail.setText(tablaProveedores.getValueAt(filaSeleccionada, 4).toString());
            txtRepresentante.setText(tablaProveedores.getValueAt(filaSeleccionada, 5).toString());
        }
    }

    private void agregarProveedor() {
        String nombre = txtNombre.getText();
        String direccion = txtDireccion.getText();
        String telefono = txtTelefono.getText();
        String email = txtEmail.getText();
        String representante = txtRepresentante.getText();

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO proveedores (nombre, direccion, telefono, email, representante) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setString(1, nombre);
            stmt.setString(2, direccion);
            stmt.setString(3, telefono);
            stmt.setString(4, email);
            stmt.setString(5, representante);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Proveedor agregado exitosamente.");
                cargarTodosLosProveedores();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al agregar proveedor: " + e.getMessage());
        }
    }

    private void modificarProveedor() {
        int filaSeleccionada = tablaProveedores.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor para modificar.");
            return;
        }

        int proveedorID = (int) tablaProveedores.getValueAt(filaSeleccionada, 0);
        String nombre = txtNombre.getText();
        String direccion = txtDireccion.getText();
        String telefono = txtTelefono.getText();
        String email = txtEmail.getText();
        String representante = txtRepresentante.getText();

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE proveedores SET nombre = ?, direccion = ?, telefono = ?, email = ?, representante = ? WHERE proveedor_id = ?")) {

            stmt.setString(1, nombre);
            stmt.setString(2, direccion);
            stmt.setString(3, telefono);
            stmt.setString(4, email);
            stmt.setString(5, representante);
            stmt.setInt(6, proveedorID);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Proveedor modificado exitosamente.");
                cargarTodosLosProveedores();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar proveedor: " + e.getMessage());
        }
    }

    private void verDetallesProveedor() {
        int filaSeleccionada = tablaProveedores.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor para ver detalles.");
            return;
        }

        int proveedorID = (int) tablaProveedores.getValueAt(filaSeleccionada, 0);
        String nombre = txtNombre.getText();
        String direccion = txtDireccion.getText();
        String telefono = txtTelefono.getText();
        String email = txtEmail.getText();
        String representante = txtRepresentante.getText();

        JOptionPane.showMessageDialog(this,
                "Detalles del Proveedor: \n" +
                        "Nombre: " + nombre + "\n" +
                        "Dirección: " + direccion + "\n" +
                        "Teléfono: " + telefono + "\n" +
                        "Email: " + email + "\n" +
                        "Representante: " + representante);
    }
}
