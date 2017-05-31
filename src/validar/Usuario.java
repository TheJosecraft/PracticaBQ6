package validar;

import java.io.Serializable;

/**
 * Define los atributos de un usuario
 * @author José Carlos
 * 
 *
 */
public class Usuario implements Serializable{

	private String nombre;
	private String password;
	
	/**
	 * 
	 * @param nombre Nombre del usuario
	 * @param password Constraseña del usuario
	 */
	public Usuario(String nombre, String password) {
		this.nombre = nombre;
		this.password = password;
	}

	/**
	 * 
	 * @return Nombre del usuario
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * 
	 * @param nombre Nombre del usuario
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * 
	 * @return Contraseña del usuario
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password Contraseña del usuario
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
