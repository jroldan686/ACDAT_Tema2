package jrl.acdat.acdat_tema2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Ejercicio6Activity extends Activity implements View.OnClickListener {

    public static final String RUTAFICHERO = "http://alumno.mobi/~alumno/superior/roldan/cambio.txt";

    EditText euros, dolares;
    RadioButton eurDolar, dolarEur;
    Button convertir;
    Conversion conversor;
    Almacenamiento almacenamiento;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio6);

        euros = (EditText)findViewById(R.id.edtEuros);
        dolares = (EditText)findViewById(R.id.edtDolares);
        eurDolar = (RadioButton)findViewById(R.id.rdoEurosADolares);
        eurDolar.setChecked(true);
        dolarEur = (RadioButton)findViewById(R.id.rdoDolaresAEuros);
        convertir = (Button)findViewById(R.id.btnConvertir);
        convertir.setOnClickListener(this);

        almacenamiento = new Almacenamiento(getFilesDir().getAbsolutePath());
        descarga();
        try {
            if(almacenamiento.getCambio() != null)
                conversor = new Conversion(Double.parseDouble(almacenamiento.getCambio()));
            else
                Toast.makeText(Ejercicio6Activity.this, "El valor es nulo", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(Ejercicio6Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v == convertir)
        {
            if (eurDolar.isChecked())
            {
                String resultado;
                resultado = conversor.convertirADolares(euros.getText().toString());
                dolares.setText(resultado);
            }

            if (dolarEur.isChecked())
            {
                String resultado;
                resultado = conversor.convertirAEuros(dolares.getText().toString());
                euros.setText(resultado);
            }
        }
    }

    private void descarga() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(RUTAFICHERO, new FileAsyncHttpResponseHandler(Ejercicio6Activity.this) {

            private ProgressDialog progreso;

            @Override
            public void onStart() {
                //Toast.makeText(Ejercicio6Activity.this, "Descargando el fichero...", Toast.LENGTH_SHORT).show();
                progreso = new ProgressDialog(Ejercicio6Activity.this);
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Descargando el fichero . . .");
                progreso.show();
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, File file) {
                progreso.dismiss();
                Toast.makeText(Ejercicio6Activity.this, "No se ha podido descargar el fichero", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, File file) {
                FileInputStream fis = null;
                InputStreamReader isw = null;
                BufferedReader in = null;
                try {
                    fis = new FileInputStream(file);
                    isw = new InputStreamReader(fis, "UTF-8");
                    in = new BufferedReader(isw);
                    String linea = "";
                    while((linea = in.readLine()) != null) {
                        almacenamiento.setCambio(linea);
                    }
                    progreso.dismiss();
                    Toast.makeText(Ejercicio6Activity.this, "El fichero se ha descargado con exito", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Ejercicio6Activity.this, "Reintentando descargar el fichero:" + retryNo, Toast.LENGTH_SHORT).show();
            }
        });
    }
}