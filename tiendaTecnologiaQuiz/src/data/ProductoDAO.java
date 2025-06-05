package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import oracle.jdbc.internal.OracleTypes;

import application.Main;
import javafx.scene.control.Alert;
import model.Producto;

public class ProductoDAO {
    private Connection connection;

    public ProductoDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(Producto producto) {
        String sql = "{call PROGRAMMINGII.InsertProduct(?, ?, ?, ?)}";  // Procedimiento almacenado para insertar
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, producto.getReferencia());
            stmt.setString(2, producto.getNombre());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getCantidad());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Main.showAlert("Database Error", "Error al guardar el producto", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public ArrayList<Producto> fetch() {
        ArrayList<Producto> productos = new ArrayList<>();
        String sql = "{? = call PROGRAMMINGII.FetchProductos()}";  // Procedimiento almacenado que devuelve un cursor
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.registerOutParameter(1, OracleTypes.CURSOR);  // Registro del parámetro OUT (el cursor)
            cs.execute();
            
            // Obtenemos el ResultSet a partir del cursor
            try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                while (rs.next()) {
                    int referencia = rs.getInt("referencia");
                    String nombre = rs.getString("nombre");
                    double precio = rs.getDouble("precio");
                    int cantidad = rs.getInt("cantidad");

                    // Crear el objeto Producto y agregarlo a la lista
                    Producto producto = new Producto(referencia, nombre, precio, cantidad);
                    productos.add(producto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Main.showAlert("Database Error", "Error al obtener los productos", e.getMessage(), Alert.AlertType.ERROR);
        }
        return productos;  // Retornamos la lista de productos
    }

    public void delete(int referencia) {
        String sql = "{call PROGRAMMINGII.DeleteProduct(?)}";  // Procedimiento almacenado para eliminar
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, referencia);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Main.showAlert("Database Error", "Error al eliminar el producto", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void update(Producto producto) {
        String sql = "{call PROGRAMMINGII.UpdateProduct(?, ?, ?, ?)}";  // Procedimiento almacenado para actualizar
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, producto.getReferencia());
            stmt.setString(2, producto.getNombre());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getCantidad());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Main.showAlert("Database Error", "Error al actualizar el producto", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public boolean authenticate(int referencia) {
        String sql = "{? = call PROGRAMMINGII.AuthenticateProduct(?)}";  // Procedimiento almacenado para autenticación
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(2, referencia);  // Establecemos la referencia que estamos buscando
            cs.registerOutParameter(1, java.sql.Types.INTEGER);  // Registramos el parámetro OUT como un valor INTEGER
            cs.execute();
            
            // Obtenemos el valor del parámetro OUT
            int result = cs.getInt(1);
            return result == 1;  // Si el valor es 1, autenticación exitosa, si es 0, falla
        } catch (SQLException e) {
            e.printStackTrace();
            Main.showAlert("Database Error", "Error en el proceso de autenticación", e.getMessage(), Alert.AlertType.ERROR);
        }
        return false;  // Si hay algún error o no se encuentra, retornamos falso
    }
}


/*
 ¿Cuál es la función del patrón de diseño creacional Builder?

Respuesta correcta: D. Facilita la construcción de objetos complejos mediante un proceso paso a paso.

Explicación: El patrón Builder es un patrón creacional que se utiliza para construir un objeto complejo paso a paso. Permite que un mismo proceso de construcción cree diferentes representaciones de un objeto sin acoplar el código de construcción con las clases del objeto final.

¿Cuáles de los siguientes son principios de SOLID?

Respuesta correcta: B. Responsabilidad única, segregación de interfaz e inversión de dependencia.

Explicación: SOLID es un conjunto de principios de diseño orientados a mejorar la calidad del software. Los principios de SOLID son:

S: Single Responsibility Principle (SRP) - Principio de Responsabilidad Única.

O: Open/Closed Principle (OCP) - Principio de Abierto/Cerrado.

L: Liskov Substitution Principle (LSP) - Principio de Sustitución de Liskov.

I: Interface Segregation Principle (ISP) - Principio de Segregación de Interfaces.

D: Dependency Inversion Principle (DIP) - Principio de Inversión de Dependencias.

¿Qué es un patrón Factory Method?

Respuesta correcta: B. Es un patrón de diseño creacional que proporciona una interfaz para crear objetos en una superclase, mientras permite a las subclases alterar el tipo de objetos que se crearán.

Explicación: El patrón Factory Method es un patrón de diseño creacional que permite la creación de objetos, pero delega la decisión de qué tipo de objeto crear a las subclases. Este patrón define un método para crear un objeto, pero permite que las subclases decidan qué clase instanciar.

¿Cuál es una característica de los procedimientos almacenados en SQL Server?

Respuesta correcta: D. Permiten modificar datos en la base de datos.

Explicación: Los procedimientos almacenados en SQL Server son conjuntos de instrucciones SQL que pueden ser ejecutadas repetidamente. Pueden realizar una variedad de tareas, incluyendo la modificación de datos en la base de datos. No todos los procedimientos almacenados deben devolver un valor (aunque pueden hacerlo), y pueden realizar operaciones de lectura y escritura en la base de datos.

¿Cuál es una fase del ciclo de vida del desarrollo de software?

Respuesta correcta: A. Diseño y codificación segura.

Explicación: El ciclo de vida del desarrollo de software consta de varias fases, y una de ellas es el diseño y la codificación segura. Esta fase se centra en crear un diseño de software robusto y seguro, así como en codificar el software de manera que se minimicen los riesgos de seguridad.
 */

