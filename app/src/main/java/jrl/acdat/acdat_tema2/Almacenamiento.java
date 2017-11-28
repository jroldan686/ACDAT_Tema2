package jrl.acdat.acdat_tema2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Almacenamiento {

	static final String nuevoCambio = "cambio.txt";
	private File archivo;
	
	public Almacenamiento(String ruta) {
		archivo = new File(ruta,nuevoCambio);
	}
	
	public String getCambio(){
		BufferedReader lector = null;
		String cambio = null;
		try {
			lector = new BufferedReader(new FileReader(archivo));
			String linea;
			while ((linea = lector.readLine()) != null) {
				cambio = linea;
			}
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally{
			if (lector != null) {
				try {
					lector.close();
				} catch (IOException e) {
					return null;
				}
			}
		}
		
		return cambio;
	}

	public void setCambio(String cambio){
		BufferedWriter escritor = null;
		
		try {
			escritor = new BufferedWriter(new FileWriter(archivo,false));
			escritor.write(cambio);
		} catch (IOException e) {

		} finally{
			if (escritor !=null) {
				try {
					escritor.close();
				} catch (IOException e) {

				}
			}
		}
		
	}
	
}
