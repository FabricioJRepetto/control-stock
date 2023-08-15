package com.alura.jdbc.modelo;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private int categoriaId;

    public Producto(String nombre, String descripcion, int cantidad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }
    public Producto(int id, String nombre, String descripcion, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    public Producto(int id, String nombre, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getCategoriaId() { return this.categoriaId; }

    public void setCategoriaId(int id) { this.categoriaId = id; }

    @Override
    public String toString() {
        return String.format("{id: %d, nombre: '%s', descripcion: '%s', cantidad: %d}%n",
                this.id, this.nombre, this.descripcion, this.cantidad);
    }

}
