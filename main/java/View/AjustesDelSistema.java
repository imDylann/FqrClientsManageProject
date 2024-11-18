/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import DataBaseUntil.DataBaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;

public class AjustesDelSistema extends JInternalFrame {
    private JTextField txtNombreEmpresa, txtDireccionEmpresa, txtTelefonoEmpresa, txtEmailEmpresa;
    private JPasswordField txtContraseñaAdmin;
    private JButton btnGuardar, btnCancelar, btnCambiarLogo, btnActualizarContraseña;
    private JCheckBox chkActivarNotificaciones;
    private JLabel lblLogo;

    public AjustesDelSistema() {
        setTitle("Ajustes del Sistema");
        setSize(1320, 750);  // Tamaño ajustado
        setLayout(new BorderLayout());

        // Panel para la información de la empresa
        JPanel panelEmpresa = new JPanel();
        panelEmpresa.setLayout(new GridBagLayout());
        panelEmpresa.setBackground(new Color(45, 52, 54));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre componentes
        gbc.anchor = GridBagConstraints.CENTER;

        // Fuentes grandes para títulos y etiquetas
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 14);

        // Componentes de la empresa
        JLabel lblNombreEmpresa = new JLabel("Nombre de la Empresa:");
        lblNombreEmpresa.setForeground(Color.WHITE);
        lblNombreEmpresa.setFont(labelFont);
        txtNombreEmpresa = new JTextField(20);
        txtNombreEmpresa.setBackground(new Color(30, 36, 42));
        txtNombreEmpresa.setForeground(Color.WHITE);
        txtNombreEmpresa.setFont(textFieldFont);

        JLabel lblDireccionEmpresa = new JLabel("Dirección:");
        lblDireccionEmpresa.setForeground(Color.WHITE);
        lblDireccionEmpresa.setFont(labelFont);
        txtDireccionEmpresa = new JTextField(20);
        txtDireccionEmpresa.setBackground(new Color(30, 36, 42));
        txtDireccionEmpresa.setForeground(Color.WHITE);
        txtDireccionEmpresa.setFont(textFieldFont);

        JLabel lblTelefonoEmpresa = new JLabel("Teléfono:");
        lblTelefonoEmpresa.setForeground(Color.WHITE);
        lblTelefonoEmpresa.setFont(labelFont);
        txtTelefonoEmpresa = new JTextField(20);
        txtTelefonoEmpresa.setBackground(new Color(30, 36, 42));
        txtTelefonoEmpresa.setForeground(Color.WHITE);
        txtTelefonoEmpresa.setFont(textFieldFont);

        JLabel lblEmailEmpresa = new JLabel("Email:");
        lblEmailEmpresa.setForeground(Color.WHITE);
        lblEmailEmpresa.setFont(labelFont);
        txtEmailEmpresa = new JTextField(20);
        txtEmailEmpresa.setBackground(new Color(30, 36, 42));
        txtEmailEmpresa.setForeground(Color.WHITE);
        txtEmailEmpresa.setFont(textFieldFont);

        // Configuración de contraseña
        JLabel lblContraseñaAdmin = new JLabel("Contraseña Admin:");
        lblContraseñaAdmin.setForeground(Color.WHITE);
        lblContraseñaAdmin.setFont(labelFont);
        txtContraseñaAdmin = new JPasswordField(20);
        txtContraseñaAdmin.setBackground(new Color(30, 36, 42));
        txtContraseñaAdmin.setForeground(Color.WHITE);
        txtContraseñaAdmin.setFont(textFieldFont);

        // Activación de notificaciones
        chkActivarNotificaciones = new JCheckBox("Activar Notificaciones");
        chkActivarNotificaciones.setBackground(new Color(45, 52, 54));
        chkActivarNotificaciones.setForeground(Color.WHITE);
        chkActivarNotificaciones.setFont(new Font("Arial", Font.PLAIN, 14));

        // Logo
        lblLogo = new JLabel("Logotipo de la Empresa");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(labelFont);
        btnCambiarLogo = new JButton("Cambiar Logotipo");
        btnCambiarLogo.setBackground(new Color(85, 239, 196));
        btnCambiarLogo.setForeground(Color.BLACK);
        btnCambiarLogo.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCambiarLogo.addActionListener(e -> cambiarLogotipo());

        // Configurar GridBagConstraints para cada componente
        gbc.gridx = 0; gbc.gridy = 0; panelEmpresa.add(lblNombreEmpresa, gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelEmpresa.add(txtNombreEmpresa, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panelEmpresa.add(lblDireccionEmpresa, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelEmpresa.add(txtDireccionEmpresa, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panelEmpresa.add(lblTelefonoEmpresa, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelEmpresa.add(txtTelefonoEmpresa, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panelEmpresa.add(lblEmailEmpresa, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panelEmpresa.add(txtEmailEmpresa, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panelEmpresa.add(lblContraseñaAdmin, gbc);
        gbc.gridx = 1; gbc.gridy = 4; panelEmpresa.add(txtContraseñaAdmin, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panelEmpresa.add(chkActivarNotificaciones, gbc);
        gbc.gridx = 1; gbc.gridy = 5; panelEmpresa.add(btnCambiarLogo, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(45, 52, 54));

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(85, 239, 196));
        btnGuardar.setForeground(Color.BLACK);
        btnGuardar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnGuardar.addActionListener(e -> guardarAjustes());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(255, 159, 67));
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCancelar.addActionListener(e -> cancelarAjustes());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // Agregar los paneles al frame
        add(panelEmpresa, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Cargar la información de la empresa
        cargarInformacionEmpresa();
    }

    private void cargarInformacionEmpresa() {
        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM empresa")) {

            if (rs.next()) {
                txtNombreEmpresa.setText(rs.getString("nombre"));
                txtDireccionEmpresa.setText(rs.getString("direccion"));
                txtTelefonoEmpresa.setText(rs.getString("telefono"));
                txtEmailEmpresa.setText(rs.getString("email"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la información de la empresa: " + e.getMessage());
        }
    }

    private void guardarAjustes() {
        String nombreEmpresa = txtNombreEmpresa.getText();
        String direccionEmpresa = txtDireccionEmpresa.getText();
        String telefonoEmpresa = txtTelefonoEmpresa.getText();
        String emailEmpresa = txtEmailEmpresa.getText();
        char[] contraseñaAdmin = txtContraseñaAdmin.getPassword();
        String contraseña = new String(contraseñaAdmin);
        boolean activarNotificaciones = chkActivarNotificaciones.isSelected();

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE empresa SET nombre = ?, direccion = ?, telefono = ?, email = ?, contraseña = ?, notificaciones = ?")) {

            stmt.setString(1, nombreEmpresa);
            stmt.setString(2, direccionEmpresa);
            stmt.setString(3, telefonoEmpresa);
            stmt.setString(4, emailEmpresa);
            stmt.setString(5, contraseña);
            stmt.setBoolean(6, activarNotificaciones);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Ajustes guardados correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudieron guardar los ajustes.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar los ajustes: " + e.getMessage());
        }
    }

    private void cancelarAjustes() {
        txtNombreEmpresa.setText("");
        txtDireccionEmpresa.setText("");
        txtTelefonoEmpresa.setText("");
        txtEmailEmpresa.setText("");
        txtContraseñaAdmin.setText("");
        chkActivarNotificaciones.setSelected(false);
    }

    private void cambiarLogotipo() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File archivoLogotipo = fileChooser.getSelectedFile();
            // Aquí puedes agregar el código para almacenar el logotipo en la base de datos
            JOptionPane.showMessageDialog(this, "Logotipo cambiado con éxito.");
        }
    }
}
