package jrl.acdat.acdat_tema2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Ejercicio1Activity extends Activity implements android.view.View.OnClickListener
{
    Button btnAnadir, btnVer;
    EditText edtNombre, edtEmail, edtTelefono;
    String nombreFichero = "Agenda.txt";
    String nombre, email, telefono;
    String datos = "";                           //Almacena los tres datos concatenados
    Memoria miMemoria = null;

    final static String DATOS = "jrl.acdat.acdat_tema2.activity_ejercicio1_verdatos";

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio1);

        edtNombre = (EditText)findViewById(R.id.edtNombre);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtTelefono = (EditText)findViewById(R.id.edtTelefono);
        btnAnadir = (Button)findViewById(R.id.btnAnadir);
        btnAnadir.setOnClickListener(this);
        btnVer = (Button)findViewById(R.id.btnVer);
        btnVer.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        if (v == btnAnadir)
        {
            try
            {
                // Se controla que se introduzcan los tres parámetros
                if (edtNombre.length() > 0 && edtEmail.length() > 0 && edtTelefono.length() > 0)
                {
                    nombre = edtNombre.getText().toString();
                    email = edtEmail.getText().toString();
                    telefono = edtTelefono.getText().toString();
                    datos = nombre + ":" + email + ":" + telefono + ";";
                    miMemoria = new Memoria(this);

                    if (miMemoria.escribirInterna(nombreFichero, datos, true, "UTF-8"))
                    {
                        Toast t = Toast.makeText(this, "Se ha guardado correctamente", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    else
                    {
                        Toast t = Toast.makeText(this, "Fallo al escribir el archivo", Toast.LENGTH_SHORT);
                        t.show();
                    }
                }
                else
                {
                    Toast t = Toast.makeText(this, "Tienes que introducir los 3 parametros", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
            catch(Exception e)
            {
                Toast t = Toast.makeText(this, "Fallo al introducir datos", Toast.LENGTH_SHORT);
                t.show();
            }
        }

        if (v == btnVer)
        {
            try
            {
                Intent datos = new Intent(this, Ejercicio1_verdatos.class);
                datos.putExtra(DATOS, nombreFichero);
                startActivity(datos);
            }
            catch(Exception e)
            {
                Toast.makeText(this, "ERROR AL PASAR DATOS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

