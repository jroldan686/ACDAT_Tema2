package jrl.acdat.acdat_tema2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener{

    Button btnEjercicio1, btnEjercicio2, btnEjercicio3, btnEjercicio4, btnEjercicio5, btnEjercicio6, btnEjercicio7;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEjercicio1 = (Button)findViewById(R.id.btnEjercicio1);
        btnEjercicio1.setOnClickListener(this);

        btnEjercicio2 = (Button)findViewById(R.id.btnEjercicio2);
        btnEjercicio2.setOnClickListener(this);

        btnEjercicio3 = (Button)findViewById(R.id.btnEjercicio3);
        btnEjercicio3.setOnClickListener(this);

        btnEjercicio4 = (Button)findViewById(R.id.btnEjercicio4);
        btnEjercicio4.setOnClickListener(this);

        btnEjercicio5 = (Button)findViewById(R.id.btnEjercicio5);
        btnEjercicio5.setOnClickListener(this);

        btnEjercicio6 = (Button)findViewById(R.id.btnEjercicio6);
        btnEjercicio6.setOnClickListener(this);

        btnEjercicio7 = (Button)findViewById(R.id.btnEjercicio7);
        btnEjercicio7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnEjercicio1)
           i = new Intent(MainActivity.this, Ejercicio1Activity.class);
        if(v == btnEjercicio2)
            i = new Intent(MainActivity.this, Ejercicio2Activity.class);
        if(v == btnEjercicio3)
            i = new Intent(MainActivity.this, Ejercicio3Activity.class);
        if(v == btnEjercicio4)
            i = new Intent(MainActivity.this, Ejercicio4Activity.class);
        if(v == btnEjercicio5)
            i = new Intent(MainActivity.this, Ejercicio5Activity.class);
        if(v == btnEjercicio6)
            i = new Intent(MainActivity.this, Ejercicio6Activity.class);
        if(v == btnEjercicio7)
            i = new Intent(MainActivity.this, Ejercicio7Activity.class);
        startActivity(i);
    }
}
