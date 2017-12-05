package jrl.acdat.acdat_tema2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

public class Ejercicio5Activity extends Activity implements View.OnClickListener {

    public static final String RUTAFICHERO = "http://alumno.mobi/~alumno/superior/roldan/enlaces.txt";

    TextView txvTituloEjercicio5, txvRutaFichero;
    EditText edtRutaFichero;
    ImageView imgvImagen;
    Button btnDescargar, btnAnterior, btnSiguiente;
    String rutaFichero;
    ArrayList<String> rutasImagenes;
    int contador = 0;
    int posicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio5);

        txvTituloEjercicio5 = (TextView)findViewById(R.id.txvTituloEjercicio5);
        txvRutaFichero = (TextView)findViewById(R.id.txvRutaFichero);
        edtRutaFichero = (EditText)findViewById(R.id.edtRutaFichero);
        imgvImagen = (ImageView)findViewById(R.id.imgvImagen);
        btnDescargar = (Button)findViewById(R.id.btnDescargar);
        btnDescargar.setOnClickListener(this);
        btnAnterior = (Button)findViewById(R.id.btnAnterior);
        btnAnterior.setOnClickListener(this);
        btnSiguiente = (Button)findViewById(R.id.btnSiguiente);
        btnSiguiente.setOnClickListener(this);

        edtRutaFichero.setText(RUTAFICHERO);

        btnSiguiente.setEnabled(false);
        btnAnterior.setEnabled(false);
    }

    public void onClick(View v) {
        if(v == btnDescargar) {
            btnSiguiente.setEnabled(false);
            btnAnterior.setEnabled(false);
            rutaFichero = edtRutaFichero.getText().toString();
            if (rutaFichero.isEmpty())
            {
                Toast.makeText(Ejercicio5Activity.this, "Introduzca la ruta del fichero", Toast.LENGTH_SHORT).show();
            }
            else
            {
                try {
                    URI.create(rutaFichero);
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get(rutaFichero, new FileAsyncHttpResponseHandler(Ejercicio5Activity.this) {
                        @Override
                        public void onStart() {
                            Toast.makeText(Ejercicio5Activity.this, "Descargando el fichero...", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, File file) {
                            Toast.makeText(Ejercicio5Activity.this, "No se ha podido descargar el fichero", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, File file) {

                            FileInputStream fis = null;
                            InputStreamReader isw = null;
                            BufferedReader in = null;
                            rutasImagenes = new ArrayList<String>();
                            try {
                                fis = new FileInputStream(file);
                                isw = new InputStreamReader(fis, "UTF-8");
                                in = new BufferedReader(isw);
                                String linea = null;
                                while ((linea = in.readLine()) != null) {
                                    rutasImagenes.add(linea);
                                }
                                Picasso.with(Ejercicio5Activity.this).load(rutasImagenes.get(posicion))
                                        .error(R.raw.error)
                                        .into(imgvImagen);
                                btnSiguiente.setEnabled(true);
                                btnAnterior.setEnabled(true);
                            } catch (IOException e) {
                                Log.e("Error", e.getMessage());
                            } finally {
                                try {
                                    if (in != null) {
                                        in.close();
                                    }
                                } catch (IOException e) {
                                    Log.e("Error al cerrar", e.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            Toast.makeText(Ejercicio5Activity.this, "Reintentando descargar el fichero:" + retryNo, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(Ejercicio5Activity.this, "Debes introducir una URL valida", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if(v == btnAnterior) {
            posicion = (rutasImagenes.size() + --contador) % rutasImagenes.size();
            Toast.makeText(Ejercicio5Activity.this, "Imagen " + (posicion + 1), Toast.LENGTH_SHORT).show();
            Picasso.with(Ejercicio5Activity.this).load(rutasImagenes.get(posicion))
                    .error(R.raw.error)
                    .into(imgvImagen);
        }
        if(v == btnSiguiente) {
            posicion = (rutasImagenes.size() + ++contador) % rutasImagenes.size();
            Toast.makeText(Ejercicio5Activity.this, "Imagen " + (posicion + 1), Toast.LENGTH_SHORT).show();
            Picasso.with(Ejercicio5Activity.this).load(rutasImagenes.get(posicion))
                    .error(R.raw.error)
                    .into(imgvImagen);
        }
    }
}
