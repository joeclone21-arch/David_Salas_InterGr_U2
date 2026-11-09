package modelo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class personaDAO {
	private File archivo;
	private persona persona;
	
	public personaDAO(persona persona) {
		this.persona = persona;
		archivo = new File("c:/gestionContactos");
		prepararArchivo();
	}
	
	private void prepararArchivo() {
		if (!archivo.exists()) {
			archivo.mkdir();
		}
		
		archivo = new File(archivo.getAbsolutePath(), "datosContactos.csv");
		if (!archivo.exists()) {
			try {
				archivo.createNewFile();
				String encabezado = String.format("%s;%s;%s;%s;%s", "NOMBRE", "TELEFONO", "EMAIL", "CATEGORIA", "FAVORITO");
				escribir(encabezado, false); // Crea el archivo limpio con el encabezado
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Metodo auxiliar con dos parametros
	private void escribir(String texto, boolean modoAnexar) {
		try (FileWriter escribir = new FileWriter(archivo.getAbsolutePath(), modoAnexar)) {
			escribir.write(texto + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Sobrecarga el metodo escribir para que acepte un solo parametro (compatible con llamadas antiguas)
	private void escribir(String texto) {
		escribir(texto, true);
	}

	public boolean escribirArchivo() {
		escribir(persona.datosContacto(), true);
		return true;
	}
	
	public List<persona> leerArchivo() throws IOException {
		List<persona> personas = new ArrayList<>();
		if (!archivo.exists()) {
			return personas;
		}

		String contactos = "";
		try (FileReader leer = new FileReader(archivo.getAbsolutePath())) {
			int c;
			while ((c = leer.read()) != -1) {
				contactos += String.valueOf((char) c);
			}
		}

		String[] datos = contactos.split("\n");
		// empieza desde i = 1 para saltar el encabezado
		for (int i = 1; i < datos.length; i++) {
			String contacto = datos[i].trim();
			if (!contacto.isEmpty()) {
				String[] partes = contacto.split(";");
				if (partes.length >= 5) {
					persona p = new persona();
					p.setNombre(partes[0]);
					p.setTelefono(partes[1]);
					p.setEmail(partes[2]);
					p.setCategoria(partes[3]);
					p.setFavorito(Boolean.parseBoolean(partes[4]));
					personas.add(p);
				}
			}
		}
		return personas;
	}
	
	public void actualizarContactos(List<persona> personas) throws IOException {
		// Sobrescribe el archivo completo colocando el encabezado oficial primero
		String encabezado = String.format("%s;%s;%s;%s;%s", "NOMBRE", "TELEFONO", "EMAIL", "CATEGORIA", "FAVORITO");
		escribir(encabezado, false); 

		// Vuelve a escribir los contactos restantes uno a uno
		for (persona p : personas) {
			escribir(p.datosContacto(), true);
		}
	}
	// Metodo adicional para exportar los contactos a un archivo CSV independiente
		public boolean exportarCSV(File archivoDestino) {
			try (FileWriter writer = new FileWriter(archivoDestino)) {
				writer.append("NOMBRE;TELEFONO;EMAIL;CATEGORIA;FAVORITO\n");
				List<persona> lista = leerArchivo();
				for (persona p : lista) {
					writer.append(p.datosContacto()).append("\n");
				}
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
}