package com.alura.jdbc.DAO;

import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoriaDAO {

    private Connection con;

    public CategoriaDAO(Connection con) {
        this.con = con;
    }

    public List<Categoria> listar() {
        List<Categoria> resultado = new ArrayList<>();

        try {
            PreparedStatement statement = con.prepareStatement(
                    "SELECT id, nombre FROM categoria");

            try(statement) {
                ResultSet resultSet = statement.executeQuery();

               try(resultSet) {
                  while(resultSet.next()) {
                      var categoria = new Categoria(resultSet.getInt("id"),
                              resultSet.getString("nombre"));

                      resultado.add(categoria);
                  }
               }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultado;
    }

    public List<Categoria> listarConProductos() {
        List<Categoria> resultado = new ArrayList<>();

        try {
            PreparedStatement statement = con.prepareStatement(
                    "SELECT C.id, C.nombre, P.id, P.nombre, P.cantidad FROM categoria C " +
                            "INNER JOIN producto P ON C.id = P.categoria_id");

            try(statement) {
                ResultSet resultSet = statement.executeQuery();

                try(resultSet) {
                    while(resultSet.next()) {
                        int categoriaId = resultSet.getInt("id");
                        String categoriaNombre = resultSet.getString("nombre");

                        //? 1: filtra una nueva lista sin duplicados de las categorías presentes en la query
                        //? 2: si encuentra la categoría actual, retorna la existente
                            //? 2b: si no crea una nueva y la guarda en la lista
                        var categoria = resultado
                                .stream()
                                .filter(cat -> Objects.equals(cat.getId(), categoriaId))
                                .findAny().orElseGet(() -> {
                                    Categoria cat = new Categoria(categoriaId, categoriaNombre);
                                    resultado.add(cat);

                                    return cat;
                                });


                        //? 3: agrega un nuevo producto con los datos de la query a la categoría correspondiente
                        var producto = new Producto(resultSet.getInt("p.id"),
                                resultSet.getString("p.nombre"),
                                resultSet.getInt("p.cantidad"));

                        categoria.agregarProductos(producto);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultado;
    }
}
