package jrl.acdat.acdat_tema2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class Ejercicio7Activity extends Activity {

    private static final int ABRIRFICHERO_REQUEST_CODE = 1;
    public static final String PASSWORD = "alumno2017";
    public static final String WEB = "https://alumno.mobi/~alumno/superior/roldan/upload.php";

    private TextView txvInformacion;
    private Button btnAbrir;
    private Button btnSubir;
    private String rutaFichero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio7);
        txvInformacion = (TextView) findViewById(R.id.txvInformacion);
        btnAbrir = (Button) findViewById(R.id.btnAbrir);
        btnAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscar();
                btnSubir.setEnabled(true);
            }
        });
        btnSubir = (Button) findViewById(R.id.btnSubir);
        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subir();
            }
        });
        txvInformacion.setText("Seleccione un fichero...");
        btnSubir.setEnabled(false);
    }

    public void buscar() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/html|file/txt|image/jpg|image/png|application/pdf|audio/mp3");
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(intent, ABRIRFICHERO_REQUEST_CODE);
        else
            //informar que no hay ninguna aplicación para manejar ficheros
            Toast.makeText(this, "No hay aplicación para manejar ficheros", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == ABRIRFICHERO_REQUEST_CODE)
            if (resultCode == RESULT_OK) {
                // Mostramos en la etiqueta la ruta del archivo seleccionado
                rutaFichero = data.getData().getPath();
                txvInformacion.setText("El fichero seleccionado es:\n" + rutaFichero);
            }
            else
                Toast.makeText(this, "Error: " + resultCode, Toast.LENGTH_SHORT).show();
    }

    private void subir() {
        final ProgressDialog progreso = new ProgressDialog(this);
        File myFile;
        Boolean existe = true;
        myFile = new File(Environment.getExternalStorageDirectory(), rutaFichero);
        //File myFile = new File("/path/to/file.png");
        RequestParams params = new RequestParams();
        try {
            params.put("fileToUpload", myFile);
            params.put("password", PASSWORD);
        } catch (FileNotFoundException e) {
            existe = false;
            txvInformacion.setText("Error en el fichero: " + e.getMessage());
            //Toast.makeText(this, "Error en el fichero: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (existe)
            RestClient.post(WEB, params, new TextHttpResponseHandler() {
                @Override
                public void onStart() {
                    // called before request is started
                    progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progreso.setMessage("Conectando . . .");
                    //progreso.setCancelable(false);
                    progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            RestClient.cancelRequests(getApplicationContext(), true);
                        }
                    });
                    progreso.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    // called when response HTTP status is "200 OK"
                    progreso.dismiss();
                    txvInformacion.setText(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    progreso.dismiss();
                    txvInformacion.setText("Error: " + statusCode + "\n" + t.getMessage());
                }
            });
    }
}
