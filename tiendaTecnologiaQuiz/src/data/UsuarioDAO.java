package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import model.Usuario;

public class UsuarioDAO {
	private Connection connection;

	public UsuarioDAO(Connection connection) {
		this.connection = connection;
	}

	public boolean authenticate(String nickname, String contraseña) {
	    // Llamada al procedimiento almacenado
	    String sql = "{? = call AuthenticateUsuario(?, ?)}";  // Procedimiento almacenado que retorna 1 o 0
	    
	    try (CallableStatement stmt = connection.prepareCall(sql)) {
	        // Registramos el parámetro OUT para obtener el resultado (1 o 0)
	        stmt.registerOutParameter(1, java.sql.Types.INTEGER);
	        
	        // Establecemos los parámetros de entrada
	        stmt.setString(2, nickname);
	        stmt.setString(3, contraseña);
	        
	        // Ejecutamos el procedimiento
	        stmt.execute();
	        
	        // Obtenemos el resultado (1 o 0) del parámetro OUT
	        int result = stmt.getInt(1);
	        
	        // Retornamos true si el resultado es 1 (usuario válido), false si es 0 (usuario no válido)
	        return result == 1;
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    // En caso de error, retornamos false
	    return false;
	}


}
