/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TerminalTest;

/**
 *
 * @author PC
 */
import Model.Cliente;
import Model.DAO.ClienteDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainTest {
    private static ClienteDAO clienteDAO = new ClienteDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion = 0;
        do {
            mostrarMenu();
            opcion = scanner.nextInt();
            scanner.nextLine();  // Limpiar el buffer
            try {
                switch (opcion) {
                    case 1 -> agregarCliente();
                    case 2 -> verClientes();
                    case 3 -> actualizarCliente();
                    case 4 -> eliminarCliente();
                    case 5 -> System.out.println("Saliendo del programa...");
                    default -> System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (SQLException e) {
                System.out.println("Error al interactuar con la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        } while (opcion != 5);
    }

    private static void mostrarMenu() {
        System.out.println("\n--- Menú de Gestión de Clientes ---");
        System.out.println("1. Agregar Cliente");
        System.out.println("2. Ver Todos los Clientes");
        System.out.println("3. Actualizar Cliente");
        System.out.println("4. Eliminar Cliente");
        System.out.println("5. Salir");
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
        scanner.nextLine();  // Limpiar el buffer

        Cliente cliente = clienteDAO.obtenerClientePorId(id);
        if (cliente == null) {
            System.out.println("Cliente con ID " + id + " no encontrado.");
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
        scanner.nextLine();  // Limpiar el buffer

        Cliente cliente = clienteDAO.obtenerClientePorId(id);
        if (cliente == null) {
            System.out.println("Cliente con ID " + id + " no encontrado.");
            return;
        }

        clienteDAO.eliminarCliente(id);
        System.out.println("Cliente eliminado exitosamente.");
    }
}
