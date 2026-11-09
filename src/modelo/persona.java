package modelo;

//Definicion de la clase publica persona
public class persona {
	
	// Declaracion de variables privadas de la clase persona
	private String nombre, telefono, email, categoria;
	private boolean favorito;
	
	// Constructor publico de la clase persona
	public persona() {
		super(); // Llama al constructor de la clase base (Object)
		//Inicializa las varaibles 
		this.nombre = "";
		this.telefono = "";
		this.email = "";
		this.categoria = "";
		this.favorito = false;
	}
	// Constructor publico de la clase persona que incializa todos los campos 
	public persona(String nombre, String telefono, String email, String categoria, boolean favorito) {
		super();// Llama al constructor de la clase base (Object)
		//Incializa las variables con los valores enviado por los argumentos
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
		this.categoria = categoria;
		this.favorito = favorito;
	}
	
	// Metodo publico para obtener el valor de nombre
	public String getNombre() {
		return nombre; // Retorna el valor de la variable nombre
	}
	
	// Metodo publico para establecer el valor de nombre
	public void setNombre(String nombre) {
		this.nombre = nombre; // Asigna el valor del parametro nombre a la variable de instancia nombre
	}
	
	// Metodo publico para obtener el valor de telefono
	public String getTelefono() {
		return telefono; // Retorna el valor de la variable telefono
	}
	
	// Metodo publico para establecer el valor de telefono
	public void setTelefono(String telefono) {
		this.telefono = telefono; // Asigna el valor del parámetro telefono a la variable de instancia telefono
	}
	
	// Metodo publico para obtener el valor de email
	public String getEmail() {
		return email; // Retorna el valor de la variable email
	}
	
	// Metodo publico para establecer el valor de email
	public void setEmail(String email) {
		this.email = email; // Asigna el valor del parámetro email a la variable de instancia email
	}
	
	// Metodo publico para obtener el valor de categoria
	public String getCategoria() {
		return categoria; // Retorna el valor de la variable categoria
	}
	
	

	// Metodo publico para establecer el valor de categoria
	public void setCategoria(String categoria) {
		this.categoria = categoria; // Asigna el valor del parámetro categoria a la variable de instancia categoria
	}
	
	// Metodo publico para verificar si el contacto es favorito
	public boolean isFavorito() {
		return favorito; // Retorna el valor de la variable favorito
	}
	
	// Metodo publico para establecer si el contacto es favorito
	public void setFavorito(boolean favorito) {
		this.favorito = favorito; // Asigna el valor del parámetro favorito a la variable de instancia favorito
	}
	
	// Metodo para proveer un formato para almacenar en un archivo
	public String datosContacto() {
		
		// Estructurar el siguiente formato: nombre;telefono;email;categoria;favorito
		// Por ejemplo: Daniela Perez;097145478;dperez2024@gmail.com;amigo;true
		String contacto = String.format("%s;%s;%s;%s;%s", nombre, telefono, email, categoria, favorito); // Crea una cadena formateada con los valores de las variables
		return contacto; // Retorna la cadena formateada
	}
	//Metodo para proveer el formato de los campos que se van a imprimir en la lista
	public String formatoLista() {
		String contacto= String.format("%-40s%-40s%-40s%-40s", nombre, telefono, email, categoria);
		return contacto;
	}

}
