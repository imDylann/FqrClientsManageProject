/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author PC
 */
import javax.swing.*;

public class ClienteIDDialog extends JDialog {
    private int clienteID;

    public ClienteIDDialog() {
        setTitle("Ingresar ID del Cliente");
        setModal(true);
        setSize(300, 150);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblMensaje = new JLabel("ID del Cliente:");
        lblMensaje.setBounds(30, 30, 100, 25);
        add(lblMensaje);

        JTextField txtClienteID = new JTextField();
        txtClienteID.setBounds(130, 30, 120, 25);
        add(txtClienteID);

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(90, 70, 100, 30);
        add(btnAceptar);

        btnAceptar.addActionListener(e -> {
            try {
                clienteID = Integer.parseInt(txtClienteID.getText());
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public int getClienteID() {
        return clienteID;
    }
}