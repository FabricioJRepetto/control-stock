package com.alura.jdbc.controller;

import com.alura.jdbc.DAO.ProductoDAO;
import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

import java.util.List;

public class ProductoController {
	private final ProductoDAO productoDAO;

	public ProductoController() {
		this.productoDAO = new ProductoDAO(new ConnectionFactory().recuperarConexion());
	}

	public int modificar(Producto producto) {
		return productoDAO.modificar(producto);
	}

	public int eliminar(Integer id) {
		return productoDAO.eliminar(id);
	}

	public List<Producto> listar() {
		return productoDAO.listar();
	}

    public void guardar(Producto producto, int id) {
		producto.setCategoriaId(id);
		productoDAO.guardar(producto);
	}

	public List<Producto> listar(Categoria categoria) {
		return productoDAO.listar(categoria.getId());
	}

}
