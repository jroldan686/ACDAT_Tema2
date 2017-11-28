package jrl.acdat.acdat_tema2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Ejercicio3Activity extends Activity {

    TextView txvEsHoyLectivo;
    DatePicker dtpFechaInicio, dtpFechaFin;
    ListView lsvLectivos;
    Button btnObtenerLectivos;
    Memoria memoria;
    Resultado resultado;
    ArrayList<Date> diasNoLectivos;
    ArrayList<Date> diasLectivos;
    Calendar calendario;
    SimpleDateFormat formato;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio3);
        txvEsHoyLectivo = (TextView)findViewById(R.id.txvEsHoyLectivo);
        dtpFechaInicio = (DatePicker)findViewById(R.id.dtpFechaInicio);
        dtpFechaFin = (DatePicker)findViewById(R.id.dtpFechaFin);
        lsvLectivos = (ListView)findViewById(R.id.lsvLectivos);
        btnObtenerLectivos = (Button)findViewById(R.id.btnObtenerLectivos);
        btnObtenerLectivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diasNoLectivos = obtenerDiasNoLectivos();
                calendario.set(dtpFechaInicio.getYear(), dtpFechaInicio.getMonth(), dtpFechaInicio.getDayOfMonth());
                Date fechaInicio = calendario.getTime();
                calendario.set(dtpFechaFin.getYear(), dtpFechaFin.getMonth(), dtpFechaFin.getDayOfMonth());
                Date fechaFin = calendario.getTime();
                diasLectivos = obtenerDiasLectivos(diasNoLectivos, fechaInicio, fechaFin);
                String[] fechas = new String[diasLectivos.size()];
                for(int i = 0; i < diasLectivos.size(); i++) {
                    fechas[i] = formato.format(diasLectivos.get(i));
                }
                // Se comprueba si el día de hoy es lectivo
                if(diasLectivos.contains(new Date((Calendar.getInstance()).getTime().getTime()))) {
                    txvEsHoyLectivo.setText("Hoy es lectivo");
                } else {
                    txvEsHoyLectivo.setText("Hoy no es lectivo");
                }
                // Se muestran los días lectivos
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Ejercicio3Activity.this, android.R.layout.simple_list_item_1, fechas);
                lsvLectivos.setAdapter(adapter);
            }
        });
        memoria = new Memoria(this);
        resultado = new Resultado();
        diasNoLectivos = new ArrayList<Date>();
        diasLectivos = new ArrayList<Date>();
        calendario = new GregorianCalendar(TimeZone.getDefault());
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        formato = new SimpleDateFormat("EEEE, dd MMMM yyyy");

        acotarCurso();
    }

    private void acotarCurso() {
        // Se introducen las fechas mínimas en los DatePickers
        calendario.set(2017, 8, 11);
        dtpFechaInicio.setMinDate(calendario.getTimeInMillis());
        dtpFechaFin.setMinDate(calendario.getTimeInMillis());
        // Se introducen las fechas máximas en los DatePickers
        calendario.set(2018, 5, 25);
        dtpFechaInicio.setMaxDate(calendario.getTimeInMillis());
        dtpFechaFin.setMaxDate(calendario.getTimeInMillis());
    }

    private ArrayList<Date> obtenerDiasNoLectivos() {
        resultado = memoria.leerRaw("nolectivos");
        String[] linea = resultado.getContenido().split("\n");
        ArrayList<Date> fechas = new ArrayList<Date>();
        for(int i = 0; i < linea.length; i++) {
            int dia = Integer.valueOf(linea[i].split(":")[0].split("/")[0]);
            int mes = Integer.valueOf(linea[i].split(":")[0].split("/")[1]);
            int anno = Integer.valueOf(linea[i].split(":")[0].split("/")[2]);
            calendario.set(anno, mes, dia);
            fechas.add(calendario.getTime());
        }
        return fechas;
    }

    private ArrayList<Date> obtenerDiasLectivos(ArrayList<Date> diasNoLectivos, Date fechaInicio, Date fechaFin) {
        ArrayList<Date> diasLectivos = new ArrayList<Date>();
        Date fecha = new Date();
        boolean esLectivo = false;
        fecha.setTime(fechaInicio.getTime());
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        while(fecha.compareTo(fechaFin) == -1) {
            for(int i = 0; i < diasNoLectivos.size(); i++) {
                if(formato.format(fecha).equals(formato.format(diasNoLectivos.get(i)))) {
                    esLectivo = false;
                    break;
                } else {
                    esLectivo = true;
                }
            }
            if(esLectivo) {
                diasLectivos.add(fecha);
            }
            calendario.setTime(fecha);
            calendario.add(Calendar.DAY_OF_YEAR, 1);
            fecha = new Date(calendario.getTime().getTime());
        }
        return diasLectivos;
    }
}
