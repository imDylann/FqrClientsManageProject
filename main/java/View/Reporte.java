package View;

import DataBaseUntil.DataBaseConnection;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Font;


public class Reporte extends JInternalFrame {
    private JTextField txtClienteID, txtCuentaID;
    private JButton btnGenerarReporte;

    public Reporte() {
        // Configuración del JInternalFrame
        setTitle("Generar Reporte de Cuenta");
        setSize(1320, 750);
        setLayout(null); // Usar diseño absoluto
        setBackground(new Color(240, 240, 240));
        setClosable(true);
        setIconifiable(true);

        // Panel superior con el título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(70, 130, 180));
        panelTitulo.setBounds(0, 0, 1320, 70);
        JLabel lblTitulo = new JLabel("Generar Reporte de Cuenta");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo);

        // Panel central para los campos de texto
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(null);
        panelCentral.setBackground(new Color(240, 240, 240));
        panelCentral.setBounds(50, 100, 1220, 500);

        JLabel lblClienteID = new JLabel("ID del Cliente:");
        lblClienteID.setFont(new Font("Arial", Font.BOLD, 18));
        lblClienteID.setBounds(50, 50, 200, 30);
        panelCentral.add(lblClienteID);

        txtClienteID = new JTextField();
        txtClienteID.setFont(new Font("Arial", Font.PLAIN, 16));
        txtClienteID.setBounds(250, 50, 300, 30);
        panelCentral.add(txtClienteID);

        JLabel lblCuentaID = new JLabel("ID de la Cuenta:");
        lblCuentaID.setFont(new Font("Arial", Font.BOLD, 18));
        lblCuentaID.setBounds(50, 100, 200, 30);
        panelCentral.add(lblCuentaID);

        txtCuentaID = new JTextField();
        txtCuentaID.setFont(new Font("Arial", Font.PLAIN, 16));
        txtCuentaID.setBounds(250, 100, 300, 30);
        panelCentral.add(txtCuentaID);

        add(panelCentral);

        // Panel inferior con el botón
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(240, 240, 240));
        panelBoton.setBounds(0, 650, 1320, 70);
        btnGenerarReporte = new JButton("Generar Reporte");
        btnGenerarReporte.setBackground(new Color(70, 130, 180));
        btnGenerarReporte.setForeground(Color.WHITE);
        btnGenerarReporte.setFont(new Font("Arial", Font.BOLD, 20));
        btnGenerarReporte.setFocusPainted(false);
        btnGenerarReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });
        panelBoton.add(btnGenerarReporte);
        add(panelBoton);
    }

    private void generarReporte() {
        String clienteID = txtClienteID.getText().trim();
        String cuentaID = txtCuentaID.getText().trim();

        if (clienteID.isEmpty() || cuentaID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese ambos ID de Cliente y Cuenta.");
            return;
        }

        try {
            // Conexión a la base de datos
            Connection conn = DataBaseConnection.getConnection();

            // Obtener el nombre del cliente
            String consultaCliente = "SELECT nombre FROM clientes WHERE cliente_id = ?";
            PreparedStatement stmtCliente = conn.prepareStatement(consultaCliente);
            stmtCliente.setInt(1, Integer.parseInt(clienteID));
            ResultSet rsCliente = stmtCliente.executeQuery();

            if (!rsCliente.next()) {
                JOptionPane.showMessageDialog(this, "No se encontró el cliente especificado.");
                return;
            }

            String nombreCliente = rsCliente.getString("nombre");

            // Información de la cuenta
            String consultaCuenta = "SELECT * FROM cuentas WHERE cuenta_id = ? AND cliente_id = ?";
            PreparedStatement stmtCuenta = conn.prepareStatement(consultaCuenta);
            stmtCuenta.setInt(1, Integer.parseInt(cuentaID));
            stmtCuenta.setInt(2, Integer.parseInt(clienteID));
            ResultSet rsCuenta = stmtCuenta.executeQuery();

            if (!rsCuenta.next()) {
                JOptionPane.showMessageDialog(this, "No se encontró la cuenta especificada para el cliente.");
                return;
            }

            double saldoActual = rsCuenta.getDouble("monto");

            // Información de los registros de cambios
            String consultaCambios = "SELECT * FROM registrocambioscuenta WHERE cuenta_id = ?";
            PreparedStatement stmtCambios = conn.prepareStatement(consultaCambios);
            stmtCambios.setInt(1, Integer.parseInt(cuentaID));
            ResultSet rsCambios = stmtCambios.executeQuery();

            // Crear PDF
            Document documento = new Document();
            String nombreArchivo = "Reporte_Cliente_" + clienteID + "_Cuenta_" + cuentaID + ".pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
            documento.open();

            // Información de la empresa
            documento.add(new Paragraph("Nombre de Empresa: Inversiones FQR", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            documento.add(new Paragraph("TEL: 88137450"));
            documento.add(new Paragraph(" "));

            // Agregar el nombre del cliente al reporte
            documento.add(new Paragraph("Cliente: " + nombreCliente, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            documento.add(new Paragraph(" "));

            // Tabla de registros de cambios
            documento.add(new Paragraph("Registros de Cambios", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            PdfPTable tabla = new PdfPTable(4); // Ahora tiene cuatro columnas
            tabla.setWidthPercentage(100);
            tabla.addCell("Fecha");
            tabla.addCell("Descripción");
            tabla.addCell("Monto");
            tabla.addCell("Cuenta ID");

            while (rsCambios.next()) {
                tabla.addCell(String.valueOf(rsCambios.getDate("fecha")));
                tabla.addCell(rsCambios.getString("descripcion"));
                tabla.addCell(String.valueOf(rsCambios.getDouble("monto")));
                tabla.addCell(String.valueOf(rsCambios.getInt("cuenta_id")));
            }
            documento.add(tabla);

            // Agregar saldo actual con la fecha de generación
            documento.add(new Paragraph(" "));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaGeneracion = sdf.format(new Date());

            String saldoFormateado = "₡" + String.format("%,d", Math.round(saldoActual));

            documento.add(new Paragraph("Saldo Actual: " + saldoFormateado));
            documento.add(new Paragraph("Fecha de Generación: " + fechaGeneracion));
            documento.add(new Paragraph(" "));

            documento.add(new Paragraph("Firma del encargado: ___________________________"));

            documento.close();
            JOptionPane.showMessageDialog(this, "Reporte generado exitosamente: " + nombreArchivo);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage());
        }
    }
}
