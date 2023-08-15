package com.alura.jdbc.modelo;

import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private int id;
    private String nombre;
    private List<Producto> productos;

    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public void agregarProductos(Producto producto) {
        if (this.productos == null) {
            this.productos = new ArrayList<Producto>();
        }

        this.productos.add(producto);
    }

    public List<Producto> getProductos() {
        return this.productos;
    }
}
