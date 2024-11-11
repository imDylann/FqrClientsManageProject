/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;

/**
 *
 * @author PC
 */
public class Cuenta {
    private int id;
    private int clienteId;
    private String tipoTransaccion; // Ejemplo: "entrada" o "salida"
    private double monto;
    private String descripcion;
    private java.util.Date fecha;

    // Constructor
    public Cuenta(int id, int clienteId, String tipoTransaccion, double monto, String descripcion, java.util.Date fecha) {
        this.id = id;
        this.clienteId = clienteId;
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getTipoTransaccion() { return tipoTransaccion; }
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public java.util.Date getFecha() { return fecha; }
    public void setFecha(java.util.Date fecha) { this.fecha = fecha; }
}
