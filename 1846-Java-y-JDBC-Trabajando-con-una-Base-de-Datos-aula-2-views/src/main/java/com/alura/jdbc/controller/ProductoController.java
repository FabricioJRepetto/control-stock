package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public void modificar(String nombre, String descripcion, Integer id) throws SQLException {
		Connection con = new ConnectionFactory().recuperarConexion();

		Statement statement = con.createStatement();

		statement.execute("UPDATE producto SET nombre = '" + nombre + "', descripcion = '" + descripcion + "' WHERE id = " + id);

		System.out.printf("%d item(s) actualizado %n", statement.getUpdateCount());
	}

	public int eliminar(Integer id) throws SQLException {
		Connection con = new ConnectionFactory().recuperarConexion();

		Statement statement = con.createStatement();

		statement.execute("DELETE FROM producto WHERE id = "+ id);

		return statement.getUpdateCount();
	}

	public List<Map<String, String>> listar() throws SQLException {
		Connection con = new ConnectionFactory().recuperarConexion();

		Statement statement = con.createStatement();

		boolean response = statement.execute("SELECT id, nombre, descripcion, cantidad FROM producto");

		ResultSet resultSet = statement.getResultSet();

		List<Map<String, String>> resultado = new ArrayList<>();

		while(resultSet.next()) {
			Map<String, String> fila = new HashMap<>();
			fila.put("id", String.valueOf(resultSet.getInt("id")));
			fila.put("nombre", resultSet.getString("nombre"));
			fila.put("descripcion", resultSet.getString("descripcion"));
			fila.put("cantidad", String.valueOf(resultSet.getInt("cantidad")));

			resultado.add(fila);
		}

		con.close();

		return resultado;
	}

    public void guardar(Map<String, String> producto) throws SQLException {
		Connection con = new ConnectionFactory().recuperarConexion();

		Statement statement = con.createStatement();

		statement.execute("INSERT INTO producto(nombre, descripcion, cantidad)" +
				"VALUES('"+producto.get("nombre")+"', '"+
				producto.get("descripcion")+"', "+
				producto.get("cantidad")+")", Statement.RETURN_GENERATED_KEYS);

		ResultSet resultSet = statement.getGeneratedKeys();

		while(resultSet.next()) {
			System.out.printf("Nuevo producto con ID: %d%n",
					resultSet.getInt(1));
		}
	}

}
