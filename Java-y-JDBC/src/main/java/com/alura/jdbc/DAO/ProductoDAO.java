package com.alura.jdbc.DAO;

import com.alura.jdbc.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//? DAO (data access object)
public class ProductoDAO {
    final private Connection con;

    public ProductoDAO(Connection con) {
        this.con = con;
    }

    public void guardar(Producto producto) {

        //? Try-with-resources
        //	se encarga de cerrar automáticamente recursos
        try {
            // Para prevenir problemas si surge un error en medio de la transacción
            con.setAutoCommit(false);

            // Para prevenir "sql injection" utilizamos un PreparedStatement
            final PreparedStatement statement = con.prepareStatement("INSERT INTO PRODUCTO "
                    + "(nombre, descripcion, cantidad, categoria_id)"
                    + " VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            try (statement) {
                ejecutarRegistro(producto, statement);

                System.out.println("commit");
                // es necesario declarar de manera explícita el "commit" en caso de éxito
                con.commit();
            } catch (SQLException e) {
                System.out.println("Rollback");
                e.printStackTrace();
                // es necesario declarar de manera explícita el "rollback" en caso de error
                con.rollback();
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void ejecutarRegistro(Producto producto, PreparedStatement statement) throws SQLException {
        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getDescripcion());
        statement.setInt(3, producto.getCantidad());
        statement.setInt(4, producto.getCategoriaId());

        statement.execute();

        final ResultSet resultSet = statement.getGeneratedKeys();

        try(resultSet) {
            while (resultSet.next()) {
                producto.setId(resultSet.getInt(1));
                System.out.printf("Nuevo producto con ID: %s", producto);
            }
        }

    }

    public List<Producto> listar() {
        try {
            final PreparedStatement statement = con.prepareStatement("SELECT id, nombre, descripcion, cantidad FROM producto");

            try(statement) {
                statement.execute();

                ResultSet resultSet = statement.getResultSet();

                List<Producto> productos = new ArrayList<>();

                while (resultSet.next()) {
                    Producto producto = new Producto(resultSet.getInt("id"),
                            resultSet.getString("nombre"),
                            resultSet.getString("descripcion"),
                            resultSet.getInt("cantidad"));

                    productos.add(producto);
                }

                return productos;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Producto> listar(int categoriaId) {
        try {
            final PreparedStatement statement = con.prepareStatement("SELECT id, nombre, descripcion, cantidad FROM producto WHERE categoria_id = ?");

            try(statement) {
                statement.setInt(1, categoriaId);

                ResultSet resultSet = statement.executeQuery();

                List<Producto> productos = new ArrayList<>();

                while (resultSet.next()) {
                    Producto producto = new Producto(resultSet.getInt("id"),
                            resultSet.getString("nombre"),
                            resultSet.getString("descripcion"),
                            resultSet.getInt("cantidad"));

                    productos.add(producto);
                }

                return productos;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int modificar(Producto producto) {
        try {
            final PreparedStatement statement = con.prepareStatement("UPDATE producto SET nombre = ?, descripcion = ?, cantidad = ? WHERE id = ?");

            try (statement) {
                statement.setString(1, producto.getNombre());
                statement.setString(2, producto.getDescripcion());
                statement.setInt(3, producto.getCantidad());
                statement.setInt(4, producto.getId());

                statement.execute();

                System.out.printf("%d item(s) actualizado %s%n", statement.getUpdateCount(), producto);
                return statement.getUpdateCount();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int eliminar(int id) {
        try {
            final PreparedStatement statement = con.prepareStatement("DELETE FROM producto WHERE id = ?");

            try(statement) {
                statement.setInt(1, id);

                statement.execute();

                return statement.getUpdateCount();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
