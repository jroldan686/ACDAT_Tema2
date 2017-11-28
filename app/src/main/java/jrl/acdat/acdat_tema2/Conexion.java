package jrl.acdat.acdat_tema2;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class Conexion {

	public static String conectarJava(String texto) 
	{
		URL url;
		HttpURLConnection urlConnection = null;
		int respuesta;
		String body = " ";
		
		try 
		{
			url = new URL(texto);
			urlConnection = (HttpURLConnection) url.openConnection();
			respuesta = urlConnection.getResponseCode();
			if (respuesta == HttpURLConnection.HTTP_OK)
				body = leer(urlConnection.getInputStream());
			else
				body = "Error en el acceso a la web: "
						+ String.valueOf(respuesta);
		} 
		catch (MalformedURLException e) 
		{
			body = "Excepcion por URL incorrecta: " + e.getMessage();
		} 
		catch (SocketTimeoutException e) 
		{
			body = "Excepcion por timeout: " + e.getMessage();
		} 
		catch (Exception e) 
		{
			body = "Excepcion: " + e.getMessage();
		} 
		finally 
		{
			try 
			{
				if (urlConnection != null)
					urlConnection.disconnect();
			} 
			catch (Exception e) 
			{
				body = "Excepcion: " + e.getMessage();
			}

		}
		return body;
	}
	
	public static String conectarApache(String texto) 
	{
		HttpClient cliente;
		/*HttpPost*/HttpGet peticion;
		HttpResponse respuesta;
		String body = " ";
		int codigo;
		try 
		{
			cliente = new DefaultHttpClient();
			peticion = new HttpGet(texto);
			respuesta = cliente.execute(peticion);
			codigo = respuesta.getStatusLine().getStatusCode();
			if (codigo == HttpURLConnection.HTTP_OK)
				body = leer(respuesta.getEntity().getContent());
			else
				body = "Error en el acceso a la web: " + String.valueOf(codigo);
		} 
		catch (Exception e) 
		{
			body = "Excepcion: " + e.getMessage();
		}
		return body;
	}

	private static String leer(InputStream inputStream) 
	{
		StringBuffer pagina = new StringBuffer();

		String line = "";
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					inputStream));
			while ((line = rd.readLine()) != null)
				pagina.append(line);
			
			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return pagina.toString();
	}
}
