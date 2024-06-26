/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package accesoadatos;

import entidades.Libros;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author elmer
 */
public class LibrosDAL {

    public static ArrayList<Libros> buscar(Libros libro) {
        ArrayList<Libros> libros = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion()) {
            String sql = "SELECT LibroID, Titulo, Autor, Genero, Publicacion, Disponible FROM Libros WHERE Titulo LIKE ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, "%" + libro.getTitulo() + "%");
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int libroID = resultSet.getInt("LibroID");
                        String titulo = resultSet.getString("Titulo");
                        String autor = resultSet.getString("Autor");
                        String genero = resultSet.getString("Genero");
                        java.sql.Date publicacion = resultSet.getDate("Publicacion");
                        boolean disponible = resultSet.getBoolean("Disponible");
                        Libros libroEncontrado = new Libros(libroID, titulo, autor, genero, new java.util.Date(publicacion.getTime()), disponible);
                        libros.add(libroEncontrado);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error al buscar los libros", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la conexión a la base de datos", e);
        }
        return libros;
    }

    public static void crear(Libros libro) {
        try (Connection conn = ComunDB.obtenerConexion()) {
            String sql = "INSERT INTO Libros (Titulo, Autor, Genero, Publicacion, Disponible) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, libro.getTitulo());
                statement.setString(2, libro.getAutor());
                statement.setString(3, libro.getGenero());
                statement.setDate(4, new java.sql.Date(libro.getPublicacion().getTime()));
                statement.setBoolean(5, libro.isDisponible());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error al crear el libro", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la conexión a la base de datos", e);
        }
    }

    public static int actualizar(Libros libro) {
        try (Connection conn = ComunDB.obtenerConexion()) {
            String sql = "UPDATE Libros SET Titulo = ?, Autor = ?, Genero = ?, Publicacion = ?, Disponible = ? WHERE LibroID = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, libro.getTitulo());
                statement.setString(2, libro.getAutor());
                statement.setString(3, libro.getGenero());
                statement.setDate(4, new java.sql.Date(libro.getPublicacion().getTime()));
                statement.setBoolean(5, libro.isDisponible());
                statement.setInt(6, libro.getLibroID());

                System.out.println("Ejecutando actualización: " + statement.toString());

                int rowsAffected = statement.executeUpdate();
                return rowsAffected;
            } catch (SQLException e) {
                throw new RuntimeException("Error al actualizar el libro", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la conexión a la base de datos", e);
        }
    }

    public static int eliminar(Libros libro) {
        try (Connection conn = ComunDB.obtenerConexion()) {
            String sql = "DELETE FROM Libros WHERE LibroID = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, libro.getLibroID());

                System.out.println("Ejecutando eliminación: " + statement.toString());

                int rowsAffected = statement.executeUpdate();
                return rowsAffected;
            } catch (SQLException e) {
                throw new RuntimeException("Error al eliminar el libro", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la conexión a la base de datos", e);
        }
    }

}
