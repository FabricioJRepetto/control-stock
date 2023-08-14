package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	private final ConnectionFactory factory = new ConnectionFactory();

	public void modificar(String nombre, String descripcion, Integer cantidad, Integer id) throws SQLException {
		final Connection con = factory.recuperarConexion();

		try(con) {
			final PreparedStatement statement = con.prepareStatement("UPDATE producto SET nombre = ?, descripcion = ?, cantidad = ? WHERE id = ?");

			try	(statement) {
				statement.setString(1, nombre);
				statement.setString(2, descripcion);
				statement.setInt(3, cantidad);
				statement.setInt(4, id);

				statement.execute();

				System.out.printf("%d item(s) actualizado %n", statement.getUpdateCount());
			}
		}
	}

	public int eliminar(Integer id) throws SQLException {
		final Connection con = factory.recuperarConexion();

		try(con) {
			final PreparedStatement statement = con.prepareStatement("DELETE FROM producto WHERE id = ?");

			try(statement) {
				statement.setInt(1, id);

				statement.execute();

				return statement.getUpdateCount();
			}
		}
	}

	public List<Map<String, String>> listar() throws SQLException {
		final Connection con = factory.recuperarConexion();

		try(con) {
			final PreparedStatement statement = con.prepareStatement("SELECT id, nombre, descripcion, cantidad FROM producto");

			try(statement) {
				statement.execute();

				ResultSet resultSet = statement.getResultSet();

				List<Map<String, String>> resultado = new ArrayList<>();

				while (resultSet.next()) {
					Map<String, String> fila = new HashMap<>();
					fila.put("id", String.valueOf(resultSet.getInt("id")));
					fila.put("nombre", resultSet.getString("nombre"));
					fila.put("descripcion", resultSet.getString("descripcion"));
					fila.put("cantidad", String.valueOf(resultSet.getInt("cantidad")));

					resultado.add(fila);
				}

				return resultado;
			}
		}
	}

    public void guardar(Map<String, String> producto) throws SQLException {
		String nombre = producto.get("nombre");
		String descripcion = producto.get("descripcion");
		int cantidad = Integer.parseInt(producto.get("cantidad"));
		Integer maximoCantidad = 50;

		final Connection con = factory.recuperarConexion();
		//? Try-with-resources
		//	se encarga de cerrar automáticamente recursos que implementan "auto-closable"
		try(con) {
			// No guardar los cambios hasta que nosotros lo hagamos
			// Para prevenir problemas si surge un error en medio de la transacción
			// seteamos el "auto-commit" en false
			// es necesario declarar de manera explícita el "commit" en caso de exito
			// o el "rollback" en caso de error
			con.setAutoCommit(false);

			// Para prevenir "sql injection" utilizamos un PreparedStatement
			// esto normaliza toda la string para ser tratada como texto
			// y no se pueda confundir como comandos sql
			final PreparedStatement statement = con.prepareStatement("INSERT INTO PRODUCTO "
					+ "(nombre, descripcion, cantidad)"
					+ " VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

			try (statement) {
				while (cantidad > 0) {
					int cantidadParaGuardar = Math.min(cantidad, maximoCantidad);

					ejecutarRegistro(nombre, descripcion, cantidadParaGuardar, statement);

					cantidad -= maximoCantidad;
				}

				con.commit();
				System.out.println("commit");
			} catch (Exception e) {
				con.rollback();
				System.out.println("rollback");
			}
		}

	}

	private static void ejecutarRegistro(String nombre, String descripcion, int cantidad, PreparedStatement statement) throws SQLException {
		statement.setString(1, nombre);
		statement.setString(2, descripcion);
		statement.setInt(3, cantidad);

		statement.execute();

		//? Try-with-resources
		//	se encarga de cerrar automáticamente recursos que implementan "auto-closable"

		// Java 7
		//	try(ResultSet resultSet = statement.getGeneratedKeys()) {
		//		while (resultSet.next()) {
		//			System.out.printf("Nuevo producto con ID: %d%n",
		//					resultSet.getInt(1));
		//		}
		//	}

		// Java 9
		final ResultSet resultSet = statement.getGeneratedKeys();
		try(resultSet) {
			while (resultSet.next()) {
				System.out.printf("Nuevo producto con ID: %d%n",
						resultSet.getInt(1));
			}
		}

	}

}
