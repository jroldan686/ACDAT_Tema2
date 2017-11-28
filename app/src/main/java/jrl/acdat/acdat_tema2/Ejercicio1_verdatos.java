package jrl.acdat.acdat_tema2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Ejercicio1_verdatos extends Activity {

    String nombreFichero;
    Memoria miMemoria = null;
    ListView lista;
    //ArrayList<String> contactos = new ArrayList<String>();
    Resultado miResultado;
    String[] contactos;
    String[] datosContacto;
    ArrayAdapter<String> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio1_verdatos);

        try
        {
            lista = (ListView)findViewById(R.id.ltvContactos);
            miMemoria = new Memoria(this);

            Intent datos = getIntent();
            nombreFichero = datos.getStringExtra(Ejercicio1Activity.DATOS);
            //contactos = miMemoria.leerInterna(nombreFichero, "UTF-8").getContenido();
            miResultado = miMemoria.leerInterna(nombreFichero, "UTF-8");

            contactos = miResultado.getContenido().split(";");
            for(int i = 0; i < contactos.length; i++) {
                datosContacto = contactos[i].split(":");
                String nombre = datosContacto[0];
                String email = datosContacto[1];
                String telefono = datosContacto[2];
                String contacto = "Nombre:   " + nombre + "\nE-mail:     " + email + "\nTelefono: " + telefono;
                contactos[i] = contacto;
                adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactos);
                lista.setAdapter(adaptador);
            }
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Los datos no han podido ser mostrados", Toast.LENGTH_SHORT).show();
        }
    }
}
