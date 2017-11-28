package jrl.acdat.acdat_tema2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Ejercicio2Activity extends Activity implements View.OnClickListener {

    Button btnAnadir, btnIniciar;
    EditText edtMinutos, edtMensaje;
    TextView txvNumAlarmas, txvCrono;
    String mensaje;
    String datosAGuardar;
    String nombreFichero = "alarmas.txt";
    Memoria miMemoria;
    Boolean anadir = true;
    ArrayList<String> arrayMensajes = new ArrayList<String>();
    ArrayList<Integer> arrayAlarmas = new ArrayList<Integer>();
    int minutos = 0;
    int alarmasRestantes = 0;
    int contAlarma = 0;
    int contMensaje = 0;
    int milisegundos = 0;
    int intervalo = 0;
    boolean contadorIniciado = false;
    CountDownTimer miContador;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio2);

        txvNumAlarmas = (TextView)findViewById(R.id.txvNumAlarmas);
        txvCrono = (TextView)findViewById(R.id.txvCrono);
        edtMinutos = (EditText)findViewById(R.id.edtMinutos);
        edtMensaje = (EditText)findViewById(R.id.edtMensaje);
        btnAnadir = (Button)findViewById(R.id.btnAnadir);
        btnAnadir.setOnClickListener(this);
        btnIniciar = (Button)findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(this);

        miMemoria = new Memoria(this);

        btnIniciar.setEnabled(false);
    }

    protected void onDestroy()
    {
        try {
            super.onDestroy();
            mp.stop();
            miContador.cancel();
        } catch (Exception e) { }
    }

    @Override
    public void onClick(View v)
    {
        if (v == btnAnadir)
        {
            try
            {
                if(edtMinutos.length() > 0 && edtMensaje.length() > 0)
                {
                    minutos = Integer.parseInt(edtMinutos.getText().toString());
                    mensaje = edtMensaje.getText().toString();
                    datosAGuardar = minutos + "," + mensaje + "\n";
                    arrayAlarmas.add(minutos);
                    arrayMensajes.add(mensaje);

                    if(miMemoria.disponibleEscritura()) {
                        if (miMemoria.escribirExterna(nombreFichero, datosAGuardar, anadir, "UTF-8")) {
                            if(!contadorIniciado)
                                btnIniciar.setEnabled(true);
                            alarmasRestantes++;
                            txvNumAlarmas.setText(Integer.toString(alarmasRestantes));
                            Toast.makeText(getApplicationContext(), "Se ha guardado correctamente", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getApplicationContext(), "Error de escritura en memoria externa", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), "No se encuentra la memoria externa", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Ambos campos no pueden estar vacios", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(), "Error al escribir en el fichero", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == btnIniciar)
        {
            try
            {
                btnIniciar.setEnabled(false);
                minutos = arrayAlarmas.get(contAlarma);
                milisegundos = minutos * 60000;
                intervalo = 1000;
                cronometroAlarma(milisegundos, intervalo);                // Inicia el cronómetro hacia atrás
            }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(), "Error al iniciar el cronómetro", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void cronometroAlarma(int milisegundos, int intervalo)
    {
        contadorIniciado = true;
        final int milis = milisegundos;
        final int interval = intervalo;
        contAlarma++;
        alarmasRestantes--;             // Resta una alarma cada vez que se inicie el contador

        miContador = new CountDownTimer(milis, intervalo)
        {
            public void onTick(long millisUntilFinished)
            {
                txvNumAlarmas.setText(Integer.toString(alarmasRestantes));
                int minutos=(int)(millisUntilFinished/1000)/60;
                int segundos=(int)(millisUntilFinished/1000)%60;
                txvCrono.setText(String.format("%02d:%02d", minutos, segundos));
            }

            public void onFinish()
            {
                mp = MediaPlayer.create(Ejercicio2Activity.this, R.raw.silbato);
                mp.start();

                AlertDialog.Builder mensaje = new AlertDialog.Builder(Ejercicio2Activity.this);

                mensaje.setTitle("Alarma terminada");
                mensaje.setMessage(arrayMensajes.get(contMensaje));
                contMensaje++;
                mensaje.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (alarmasRestantes > 0) {
                            cronometroAlarma(milis, interval);
                        } else {
                            contadorIniciado = false;
                            btnIniciar.setEnabled(true);
                            return;
                        }
                    }
                });
                mensaje.create();
                mensaje.show();
            }
        }.start();
    }
}