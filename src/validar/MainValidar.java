package validar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Clase principal
 * @author José Carlos
 * @version Release 1.0
 *
 */
public class MainValidar {

	public static void main(String[] args) {
		boolean menu = true;
		int opcion;

		
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		
		/*Directorio donde se guardará el fichero de los usuarios*/
		File directorio = new File("Usuarios");
		compruebaDirectorio(directorio);
		
		/*Directorio donde se guardarán los logs de acceso*/
		File logs = new File("Log");
		compruebaDirectorio(logs);
		
		
		/*Carga el array con los usuarios actuales del fichero*/
		File dirUsuarios = new File("Usuarios/usuarios.bin");
		
		if (dirUsuarios.exists()) {
			cargarArray(usuarios, directorio);
		}
		
		/*Menú para elegir la opción*/
		try {
			do {
				menu();
				opcion = new Scanner(System.in).nextInt();

				if (opcion < 1 && opcion > 6) {
					
					throw new InputMismatchException("Valor no válido");
					
				}
				
				System.out.println();

				switch (opcion) {
				case 1:

					darAlta(usuarios, directorio);
					System.out.println();
					pausa(3);
					break;

				case 2:
					mostar(directorio);
					pausa(3);
					break;

				case 3:
					entrar(usuarios, logs);
					pausa(3);
					break;

				case 4:
					mostrarLogAutorizado(logs);
					pausa(3);
					break;

				case 5:
					mostrarLogNoAutorizado(logs);
					pausa(3);
					break;

				case 6:
					menu = false;
					break;
				default:
					System.out.println("Opción no válida");
					break;
				}
			} while (menu);
		} catch (InputMismatchException e) {
			System.out.print("El valor que has introducido no es válido");
		}
		
	}

	/**
	 * Procedimiento que carga los usuarios del fichero en un ArrayList
	 * @param usuarios ArrayList con los usuarios que hay en el sistema
	 * @param directorio Ruta en la que estará el fichero de usuarios
	 */
	private static void cargarArray(ArrayList<Usuario> usuarios, File directorio) {
		
		FileInputStream rutaSalida = null;
		ObjectInputStream salida = null;
		try {
			rutaSalida =  new FileInputStream(directorio + "/usuarios.bin");
			salida  = new ObjectInputStream(rutaSalida);

			while (true) {
				usuarios.add((Usuario)salida.readObject());
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (EOFException e) {
			System.out.println("Lectura de fichero terminada");
		}catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rutaSalida != null) {
					rutaSalida.close();
				}

				if (salida != null) {
					salida.close();
				}

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

		}
	}
	
	
	/**
	 * Procedimiento que muestra los logs de acceso autorizado
	 * @param logs Directorio en el que se encuentran los ficheros de log
	 */
	public static void mostrarLogAutorizado(File logs){
		FileInputStream rutaSalida = null;
		DataInputStream salida = null;

		String acceso = "";

		try {
			rutaSalida = new FileInputStream(logs + "/logAccesoAutorizado.log");
			salida = new DataInputStream(rutaSalida);

			/*Va leyendo los datos del log y los va mostrando*/
			while (true) {
				acceso = salida.readUTF();
				System.out.println(acceso);
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}catch (EOFException e) {
			System.out.println("No hay más logs de acceso autorizado");
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				if (rutaSalida != null) {
					rutaSalida.close();
				}

				if (salida != null) {
					salida.close();
				}

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Procedimiento que muestra los logs de acceso no autorizado
	 * @param logs Directorio en el que se encuentran los archivos de log
	 */
	public static void mostrarLogNoAutorizado(File logs){
		FileInputStream rutaSalida = null;
		DataInputStream salida = null;

		String acceso = "";

		try {
			rutaSalida = new FileInputStream(logs + "/logAccesoNoAutorizado.log");
			salida = new DataInputStream(rutaSalida);

			/*Va leyendo los datos del log y los va mostrando*/
			while (true) {
				acceso = salida.readUTF();
				System.out.println(acceso);
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}catch (EOFException e) {
			System.out.println("No hay más logs de acceso no autorizado");
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				if (rutaSalida != null) {
					rutaSalida.close();
				}

				if (salida != null) {
					salida.close();
				}

			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}


	/**
	 * Procedimiento que recibe un ArrayList de usuarios y que permite entrar al sistema y comprobar si esos usuarios están dados de alta
	 * @param usuarios ArrayList de usuarios
	 * @param logs Directorio en el que se encuentran los ficheros de log
	 */
	public static void entrar(ArrayList<Usuario> usuarios, File logs){
		Scanner lector = new Scanner(System.in);
		
		FileOutputStream rutaEntradaAutorizado = null;
		DataOutputStream entradaAutorizado = null;

		FileOutputStream rutaEntradaNoAutorizado = null;
		DataOutputStream entradaNoAutorizado = null;

		boolean nombreExiste = false;
		boolean passExiste = false;

		String nombre = "";
		String password = "";
		String acceso;
		
		/*Se obtiene la fecha actual ( obtiene la hora del sistema ) usando el método getInstance() de la clase Calendar*/
		Calendar fecha = Calendar.getInstance();
        int año = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH) + 1;
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int horas = fecha.get(Calendar.HOUR_OF_DAY);
        int minutos = fecha.get(Calendar.MINUTE);
        int segundos = fecha.get(Calendar.SECOND);

		/*Pide al usuario que ingrese un nombre de usuario para entrar*/
		System.out.print("Introduce un nombre de usuario: ");
		nombre = lector.nextLine();

		/*Pide al usuario que ingrese una contraseña para entrar*/
		System.out.print("Introduce la contraseña: ");
		password = lector.nextLine();

		for (int i = 0; i < usuarios.size(); i++) {
			/*Comprueba si el usuario es correcto*/
			if (nombre.equals(usuarios.get(i).getNombre())) {
				nombreExiste = true;
			}

			/*Comprueba si la contraseña es correcta*/
			if (password.equals(usuarios.get(i).getPassword())) {
				passExiste = true;
			}
		}
		
		if (!nombreExiste) {
			System.out.println("El nombre no coincide");
		}

		if (!passExiste) {
			System.out.println("La contraseña no coincide");
		}

		/*Si se cumplen las condiciones se muestra un mensaje*/
		if (nombreExiste && passExiste) {
			System.out.println("Has accedido correctamente al sistema");
		}

		/*Se registran los logs de acceso*/
		try {

			rutaEntradaAutorizado = new FileOutputStream(logs + "/logAccesoAutorizado.log", true);
			entradaAutorizado = new DataOutputStream(rutaEntradaAutorizado);

			rutaEntradaNoAutorizado = new FileOutputStream(logs + "/logAccesoNoAutorizado.log", true);
			entradaNoAutorizado = new DataOutputStream(rutaEntradaNoAutorizado);

                        if (mes < 10) {
                            acceso = "Usuario: " + nombre + " Fecha: " + dia + "/" + "0" + mes + "/" + año + " Hora: " + horas + ":" + minutos + ":" + segundos + "\n";
                        }else{
                            acceso = "Usuario: " + nombre + " Fecha: " + dia + "/" + mes + "/" + año + " Hora: " + horas + ":" + minutos + ":" + segundos + "\n";
                        }
			/*String que almacena el nombre del usuario que ha realizado el acceso junto a la fecha y la hora aplicándole formato con las variables antes definidas*/
			

			/*Si el usuario que ha intentado acceder no coincide el usuario o la contraseña se guarda en el log de acceso no autorizado*/
			if (!nombreExiste || !passExiste) {
				entradaNoAutorizado.writeUTF(acceso);
			}

			/*Si los datos son correctos se guarda en el log de acceso autorizado*/
			if (nombreExiste && passExiste) {
				entradaAutorizado.writeUTF(acceso);
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				if (rutaEntradaAutorizado != null) {
					rutaEntradaAutorizado.close();
				}

				if (entradaAutorizado != null) {
					entradaAutorizado.close();
				}

				if (rutaEntradaNoAutorizado != null) {
					rutaEntradaNoAutorizado.close();
				}

				if (entradaNoAutorizado != null) {
					entradaNoAutorizado.close();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Procedimiento que recibe un ArrayList de usuarios como parámetro y permite dar de alta un usuario
	 * @param usuarios ArrayList de usuarios
	 * @param directorio Directorio en el que se encuentra el fichero de usuarios
	 */
	public static void darAlta(ArrayList<Usuario> usuarios, File directorio){
		Scanner scanner = new Scanner(System.in);
		FileOutputStream rutaEntrada = null;
		ObjectOutputStream entrada = null;

		FileInputStream rutaSalida = null;
		ObjectInputStream salida = null;

		/*Variable de tipo usuario en la que se guardarán los datos que el usuario introduzca para hacer el alta*/
		Usuario u;

		try {
			compruebaDirectorio(directorio);

			rutaEntrada = new FileOutputStream(directorio + "/usuarios.bin");
			entrada =  new ObjectOutputStream(rutaEntrada);

			String nombre = "";
			String password = "";
			int caracter;
			
			boolean nombreV = true;
			boolean passV =  true;
			boolean error = false;
			
			int contadorPass = 0;
			int contadorNum = 0;
			int contadorPal = 0;
			int contadorOtro = 0;
			
			/*Se ejecuta este código mientras que el usuario introducido sea uno que ya está en el sistema*/
			do {

				System.out.print("Introduce el nombre de usuario: ");
				nombre = scanner.nextLine();
				
				/*Comprueba si el nombre está en el sistema*/
				for (int i = 0; i < usuarios.size(); i++) {
					if (nombre.equals(usuarios.get(i).getNombre())) {
						nombreV = false;
					}else {
						nombreV = true;
					}

				}
				if (!nombreV) {
					System.out.println("Usuario repetido");
				}

			} while (!nombreV);

			/*Pide al usuario la introducción de la contraseña*/
			System.out.print("Introduce la contraseña: ");
			password = scanner.nextLine();

			caracter = nombre.charAt(0);

			/*Comprueba si el nombre tiene una longitud mínima de 8 caracteres*/
			if (nombre.length() >= 8 ) {
				/*Comprueba si el nombre empieza por una letra*/
				if ((caracter >= 65 && caracter <= 90) || (caracter >= 97 && caracter <= 122)) {
					nombreV = true;
				}
				
				if ((caracter >= 48 && caracter <= 57) || (caracter >= 65 && caracter <= 90) || (caracter >= 97 && caracter <= 122)) {
					nombreV = true;
				}else {
					nombreV = false;
					System.out.println("El nombre no es válido, compruebe que sólo ha introducido caracteres alfanuméricos");
				}

			}else {
				System.out.println("El usuario no es válido, compruebe que tiene una longitud de 8 caracteres y que empieza por una letra");
				nombreV = false;
			}

			/*Comprueba si la contraseña tiene una longitud de 8 caracteres*/
			if (password.length() != 8) {
				System.out.println("La longitud de la contraseña no es correcta, tiene que ser de 8 caracteres");
				passV = false;
			}else {
				/*Recorre la contraseña */
				for (int i = 0; i < password.length(); i++) {
					caracter = password.charAt(i);

					/*Comprueba si la contraseña contiene alguna mayúscula*/
					if (caracter >= 65 && caracter <= 90) {
						contadorPass++;
					}

					/*Comprueba si la contraseña contiene letras*/
					if ((caracter >= 65 && caracter <= 90) || (caracter >= 97 && caracter <= 122)) {
						contadorPal++;
					}
					
					/*Comprueba si la contraseña contiene números*/
					if (caracter >= 48 && caracter <= 57) {
						contadorNum++;
					}
					
					if ((caracter >= 48 && caracter <= 57) || (caracter >= 65 && caracter <= 90) || (caracter >= 97 && caracter <= 122)) {
						error = false;
					}else {
						contadorOtro++;
						error = true;
					}
					
				}
				
			}

			/*Comprueba si la contraseña cumple todas las condiciones*/
			if (contadorPass >= 1 && contadorPal >= 1 && contadorPass >= 1 && contadorOtro == 0) {
				passV = true;
			}else {
				if (contadorPass == 0) {
					System.out.println("La contraseña no contiene letras mayúculas");
				}

				if (contadorPal == 0) {
					System.out.println("La contraseña no contiene letras");
				}

				if (contadorNum == 0) {
					System.out.println("La contraseña no contiene números");
				}
				
				if (contadorOtro > 0) {
					System.out.println("la contraseña contiene caracteres no válidos");
				}

				passV = false;
			}

			/*Sie el nombre es válido se ejecuta*/
			if (nombreV) {
				/*Si la contraseña es válida se ejecuta*/
				if (passV) {
					
					/*Añade el usuario al ArrayList*/
					u = new Usuario(nombre, password);
					usuarios.add(u);

					/*Sobreescribe el fichero de usuarios y añade los usuarios del ArrayList*/
					for (int i = 0; i < usuarios.size(); i++) {
						u = usuarios.get(i);
						entrada.writeObject(u);
					}

					System.out.println("Usuario añadido correctamente");
				}

			}else {
				System.out.println("Datos incorrectos");
			}


		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (rutaEntrada != null) {
					rutaEntrada.close();
				}

				if (entrada != null) {
					entrada.close();
				}

				if (rutaSalida != null) {
					rutaSalida.close();
				}

				if (salida != null) {
					salida.close();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Procedimiento que muestra todos los usuarios en el sistema
	 * @param directorio Directorio en el que se encuentra el fichero de usuarios
	 */
	public static void mostar(File directorio){
		FileInputStream rutaSalida = null;
		ObjectInputStream salida = null;

		try {
			
			/*Comprueba si el directorio Usuarios está creado*/
			compruebaDirectorio(directorio);

			rutaSalida =  new FileInputStream(directorio + "/usuarios.bin");
			salida  = new ObjectInputStream(rutaSalida);

			Usuario u;
			System.out.println("Usuarios: ");
			System.out.println();
			
			/*Muestra los usuarios del sistema*/
			while (true) {
				u = (Usuario) salida.readObject();
				System.out.println(u.getNombre());
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (EOFException e) {
			System.out.println();
			System.out.println("No hay más usuarios");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}finally {
			try {
				if (rutaSalida != null) {
					rutaSalida.close();
				}

				if (salida != null) {
					salida.close();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Procedimiento que recibe como parámentro una ruta de un directorio y comprueba si está creado, en caso de que no lo crea.
	 * @param ruta Ruta del directorio
	 */
	public static void compruebaDirectorio(File ruta){

		if (!ruta.exists()) {
			System.out.println("El directorio " + ruta.getName() + " no existe, se va a proceder a su creación."); 
			pausa(1);
			
			System.out.println("Creando directorio " + ruta.getName() + ".");
			pausa(1);
			
			ruta.mkdir();
			System.out.println("Directorio creado.");
			pausa(1);
		}

	}

	/**
	 * Procedimiento que pausa el programa durante 3 segundos para mostrar mejor la información
	 */
	public static void pausa(int s){
		try {
			TimeUnit.SECONDS.sleep(s);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Procedimiento que muestra un menú con las opciones a elegir
	 */
	public static void menu(){
		System.out.println("1. Dar de alta usuario");
		System.out.println("2. Mostrar usuarios");
		System.out.println("3. Entrar en el sistema");
		System.out.println("4. Mostrar logs de acceso autorizado");
		System.out.println("5. Mostrar logs de acceso no autorizado");
		System.out.println("6. Salir");
		System.out.println("");
		System.out.print("Elige una opción: ");
	}
}
