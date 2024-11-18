
package View;

import DataBaseUntil.DataBaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class AdministracionCuentas extends JInternalFrame {
    private JTable tablaCuentas;
    private JTextField txtMonto, txtDescripcion;
    private JToggleButton btnDeposito, btnRetiro;
    private JButton btnAgregarCuenta, btnRealizarTransaccion;
    private int clienteIDSeleccionado;

    public AdministracionCuentas() {
        setTitle("Administración de Cuentas");
        setSize(1320, 750);
        setLayout(new BorderLayout());

        // Solicitar ID del cliente
        clienteIDSeleccionado = solicitarClienteID();
        if (clienteIDSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "No se ingresó un ID de cliente válido.");
            dispose();
            return;
        }

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

        // Botones JToggle para tipo de transacción
        btnDeposito = new JToggleButton("Depósito");
        btnDeposito.setBackground(new Color(85, 239, 196));
        btnDeposito.setForeground(Color.BLACK);
        btnDeposito.addItemListener(e -> btnRetiro.setSelected(false));

        btnRetiro = new JToggleButton("Retiro");
        btnRetiro.setBackground(new Color(255, 159, 67));
        btnRetiro.setForeground(Color.BLACK);
        btnRetiro.addItemListener(e -> btnDeposito.setSelected(false));

        // Agrupar botones en un ButtonGroup para que solo uno esté seleccionado a la vez
        ButtonGroup group = new ButtonGroup();
        group.add(btnDeposito);
        group.add(btnRetiro);

        // TextField para monto y descripción
        JLabel lblMonto = new JLabel("Monto:");
        lblMonto.setForeground(Color.WHITE);
        txtMonto = new JTextField();

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setForeground(Color.WHITE);
        txtDescripcion = new JTextField();

        // Botones para agregar cuenta y realizar transacción
        btnAgregarCuenta = new JButton("Agregar Cuenta");
        btnAgregarCuenta.setBackground(new Color(85, 239, 196));
        btnAgregarCuenta.setForeground(Color.BLACK);
        btnAgregarCuenta.addActionListener(e -> agregarCuenta());

        btnRealizarTransaccion = new JButton("Realizar Transacción");
        btnRealizarTransaccion.setBackground(new Color(255, 159, 67));
        btnRealizarTransaccion.setForeground(Color.BLACK);
        btnRealizarTransaccion.addActionListener(e -> realizarTransaccion());

        // Agregar componentes al formulario
        panelFormulario.add(lblTipoTransaccion);
        panelFormulario.add(new JPanel());  // Espacio vacío para alinear los botones
        panelFormulario.add(btnDeposito);
        panelFormulario.add(btnRetiro);
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

    private int solicitarClienteID() {
        String input = JOptionPane.showInputDialog(this, "Ingrese el ID del cliente:");
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1; // ID inválido
        }
    }

    private void cargarCuentasCliente() {
        DefaultTableModel modelo = (DefaultTableModel) tablaCuentas.getModel();
        modelo.setRowCount(0);

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT cuenta_id, tipo_transaccion, monto, fecha, descripcion FROM cuentas WHERE cliente_id = ?")) {

            stmt.setInt(1, clienteIDSeleccionado);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[] {
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
        String tipoTransaccion = btnDeposito.isSelected() ? "entrada" : (btnRetiro.isSelected() ? "salida" : "");
        if (tipoTransaccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de transacción.");
            return;
        }

        double monto = Double.parseDouble(txtMonto.getText());
        String descripcion = txtDescripcion.getText();
        LocalDate fechaActual = LocalDate.now();

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO cuentas (cliente_id, tipo_transaccion, monto, descripcion, fecha) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setInt(1, clienteIDSeleccionado);
            stmt.setString(2, tipoTransaccion);
            stmt.setDouble(3, monto);
            stmt.setString(4, descripcion);
            stmt.setDate(5, java.sql.Date.valueOf(fechaActual));

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
    String inputCuentaID = JOptionPane.showInputDialog(this, "Ingrese el ID de la cuenta:");
    if (inputCuentaID == null || inputCuentaID.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe ingresar un ID de cuenta.");
        return;
    }

    int cuentaID;
    try {
        cuentaID = Integer.parseInt(inputCuentaID);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "ID de cuenta inválido.");
        return;
    }

    String tipoTransaccion = btnDeposito.isSelected() ? "deposito" : (btnRetiro.isSelected() ? "retiro" : "");
    if (tipoTransaccion.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de transacción.");
        return;
    }

    // Obtener el monto y la descripción de la transacción
    double montoTransaccion;
    try {
        montoTransaccion = Double.parseDouble(txtMonto.getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Monto inválido.");
        return;
    }

    String descripcionTransaccion = txtDescripcion.getText();
    if (descripcionTransaccion.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe ingresar una descripción.");
        return;
    }

    double nuevoMonto = 0;

    // Obtener el monto actual de la cuenta para realizar la operación de la transacción
    try (Connection conn = DataBaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT monto FROM cuentas WHERE cuenta_id = ?")) {

        stmt.setInt(1, cuentaID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            double montoActual = rs.getDouble("monto");

            // Dependiendo de si es un depósito o un retiro, actualizar el monto
            if (tipoTransaccion.equals("deposito")) {
                nuevoMonto = montoActual + montoTransaccion;
            } else if (tipoTransaccion.equals("retiro")) {
                if (montoTransaccion > montoActual) {
                    JOptionPane.showMessageDialog(this, "No hay suficiente saldo para realizar el retiro.");
                    return;
                }
                nuevoMonto = montoActual - montoTransaccion;
            }

            // Actualizar tanto el monto como la descripción en la base de datos
            try (PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE cuentas SET monto = ?, descripcion = ? WHERE cuenta_id = ?")) {

                updateStmt.setDouble(1, nuevoMonto);
                updateStmt.setString(2, descripcionTransaccion);
                updateStmt.setInt(3, cuentaID);
                int rowsAffected = updateStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Transacción realizada exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró la cuenta con el ID proporcionado.");
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "No se encontró la cuenta con el ID proporcionado.");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al realizar la transacción: " + e.getMessage());
    }

    // Cargar nuevamente las cuentas para actualizar la vista
    cargarCuentasCliente();
}

}
