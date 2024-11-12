/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TerminalTest;

import Model.Cliente;
import Model.Cuenta;
import Model.DAO.ClienteDAO;
import Model.DAO.CuentaDAO;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainTest {
    private static ClienteDAO clienteDAO = new ClienteDAO();
    private static CuentaDAO cuentaDAO = new CuentaDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion = 0;
        do {
            mostrarMenu();
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer
            try {
                switch (opcion) {
                    case 1 -> agregarCliente();
                    case 2 -> verClientes();
                    case 3 -> actualizarCliente();
                    case 4 -> eliminarCliente();
                    case 5 -> agregarCuenta();
                    case 6 -> verCuentasDeCliente();
                    case 7 -> modificarMontoCuenta();
                    case 9 -> System.out.println("Saliendo del programa...");
                    default -> System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (SQLException e) {
                System.out.println("Error al interactuar con la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        } while (opcion != 9);
    }

    private static void mostrarMenu() {
        System.out.println("\n--- Menú Principal ---");
        System.out.println("1. Agregar Cliente");
        System.out.println("2. Ver Todos los Clientes");
        System.out.println("3. Actualizar Cliente");
        System.out.println("4. Eliminar Cliente");
        System.out.println("5. Agregar Cuenta a un Cliente");
        System.out.println("6. Ver Cuentas de un Cliente");
        System.out.println("7. Actualizar Cuenta");
        System.out.println("8. Eliminar Cuenta");
        System.out.println("9. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void agregarCliente() throws SQLException {
        System.out.println("\n--- Agregar Nuevo Cliente ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        Cliente nuevoCliente = new Cliente(0, nombre, direccion, telefono);
        clienteDAO.insertarCliente(nuevoCliente);
        System.out.println("Cliente agregado exitosamente.");
    }

    private static void verClientes() throws SQLException {
        System.out.println("\n--- Lista de Clientes ---");
        List<Cliente> clientes = clienteDAO.obtenerTodosLosClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            for (Cliente cliente : clientes) {
                System.out.println("ID: " + cliente.getId() + " | Nombre: " + cliente.getNombre() +
                        " | Dirección: " + cliente.getDireccion() + " | Teléfono: " + cliente.getTelefono());
            }
        }
    }

    private static void actualizarCliente() throws SQLException {
        System.out.println("\n--- Actualizar Cliente ---");
        System.out.print("ID del cliente a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Cliente cliente = clienteDAO.obtenerClientePorId(id);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.print("Nuevo Nombre (actual: " + cliente.getNombre() + "): ");
        String nombre = scanner.nextLine();
        System.out.print("Nueva Dirección (actual: " + cliente.getDireccion() + "): ");
        String direccion = scanner.nextLine();
        System.out.print("Nuevo Teléfono (actual: " + cliente.getTelefono() + "): ");
        String telefono = scanner.nextLine();

        cliente.setNombre(nombre.isEmpty() ? cliente.getNombre() : nombre);
        cliente.setDireccion(direccion.isEmpty() ? cliente.getDireccion() : direccion);
        cliente.setTelefono(telefono.isEmpty() ? cliente.getTelefono() : telefono);

        clienteDAO.actualizarCliente(cliente);
        System.out.println("Cliente actualizado exitosamente.");
    }

    private static void eliminarCliente() throws SQLException {
        System.out.println("\n--- Eliminar Cliente ---");
        System.out.print("ID del cliente a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Cliente cliente = clienteDAO.obtenerClientePorId(id);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        clienteDAO.eliminarCliente(id);
        System.out.println("Cliente eliminado exitosamente.");
    }

    private static void agregarCuenta() throws SQLException {
        System.out.println("\n--- Agregar Nueva Cuenta ---");
        System.out.print("ID del cliente: ");
        int clienteId = scanner.nextInt();
        scanner.nextLine();

        Cliente cliente = clienteDAO.obtenerClientePorId(clienteId);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.print("Tipo de Transacción (entrada/salida): ");
        String tipoTransaccion = scanner.nextLine();
        System.out.print("Monto: ");
        double monto = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();

        Cuenta nuevaCuenta = new Cuenta(0, clienteId, tipoTransaccion, monto, descripcion, new Date());
        cuentaDAO.insertarCuenta(nuevaCuenta);
        System.out.println("Cuenta agregada exitosamente.");
    }

    private static void verCuentasDeCliente() throws SQLException {
        System.out.println("\n--- Ver Cuentas de un Cliente ---");
        System.out.print("ID del cliente: ");
        int clienteId = scanner.nextInt();
        scanner.nextLine();

        List<Cuenta> cuentas = cuentaDAO.obtenerCuentasPorCliente(clienteId);
        if (cuentas.isEmpty()) {
            System.out.println("No se encontraron cuentas para este cliente.");
        } else {
            for (Cuenta cuenta : cuentas) {
                System.out.println("ID: " + cuenta.getId() + " | Tipo: " + cuenta.getTipoTransaccion() +
                        " | Monto: " + cuenta.getMonto() + " | Descripción: " + cuenta.getDescripcion() +
                        " | Fecha: " + cuenta.getFecha());
            }
        }
    }

    private static void modificarMontoCuenta() throws SQLException {
    System.out.println("\n--- Modificar Monto de la Cuenta ---");
    System.out.print("ID de la cuenta: ");
    int cuentaId = scanner.nextInt();
    System.out.print("Monto a modificar: ");
    double monto = scanner.nextDouble();
    scanner.nextLine(); // Limpiar buffer
    System.out.print("Tipo de transacción (entrada/salida): ");
    String tipoTransaccion = scanner.nextLine();
    System.out.print("Descripción del cambio: ");
    String descripcion = scanner.nextLine();

    if (!tipoTransaccion.equalsIgnoreCase("entrada") && !tipoTransaccion.equalsIgnoreCase("salida")) {
        System.out.println("El tipo de transacción debe ser 'entrada' o 'salida'.");
        return;
    }

    cuentaDAO.actualizarMontoCuenta(cuentaId, monto, tipoTransaccion, descripcion);
    System.out.println("Monto actualizado y registro creado exitosamente.");
}

}
