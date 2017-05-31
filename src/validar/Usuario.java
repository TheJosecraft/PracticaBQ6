package validar;

import java.io.Serializable;

/**
 * Define los atributos de un usuario
 * @author Jos� Carlos
 * 
 *
 */
public class Usuario implements Serializable{

	private String nombre;
	private String password;
	
	/**
	 * 
	 * @param nombre Nombre del usuario
	 * @param password Constrase�a del usuario
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
	 * @return Contrase�a del usuario
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password Contrase�a del usuario
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
