package jrl.acdat.acdat_tema2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Ejercicio7Activity extends Activity implements View.OnClickListener {

    private static final int ABRIRFICHERO_REQUEST_CODE = 1;
    private Button btnAbrir;
    private TextView txvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio7);
        btnAbrir = (Button) findViewById(R.id.btnAbrir);
        txvInfo = (TextView) findViewById(R.id.txvInfo);
        btnAbrir.setOnClickListener(this);
    }
    @Override
    public void onClick (View v) {
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
                String ruta = data.getData().getPath();
                txvInfo.setText(ruta);
            }
            else
                Toast.makeText(this, "Error: " + resultCode, Toast.LENGTH_SHORT).show();
    }
}
