package jrl.acdat.acdat_tema2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.net.URI;

import cz.msebera.android.httpclient.Header;
import okhttp3.OkHttpClient;

public class Ejercicio4Activity extends Activity {

    public static final String TAG = "MyTag";
    public static final int TIMEOUT = 3000;
    public static final int RETRY = 1;
    public static final int MULTIPLE = 1;

    EditText edtUrl;
    RadioButton rdjava;
    RadioButton rdvolley;
    RadioButton rdaahc;
    Button btnConectar;
    WebView paginaWeb;
    TextView txvDuracion;
    String url;
    String pagina = " ";
    String resultado = "";
    long inicio, fin;
    public final static String JAVA = "java";
    public final static String VOLLEY = "volley";
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio4);

        edtUrl = (EditText) findViewById(R.id.edt_URL);
        rdjava = (RadioButton) findViewById(R.id.rd_Java);
        rdvolley = (RadioButton) findViewById(R.id.rd_Volley);
        rdaahc = (RadioButton)findViewById(R.id.rd_AAHC);
        btnConectar = (Button) findViewById(R.id.btn_Conectar);
        paginaWeb = (WebView) findViewById(R.id.webVista);
        txvDuracion = (TextView) findViewById(R.id.txvDuracion);

        mRequestQueue = Volley.newRequestQueue(this);

        btnConectar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String tipo = "";
                TareaAsincrona tarea = new TareaAsincrona(Ejercicio4Activity.this);
                url = edtUrl.getText().toString();
                try {
                    URI.create(url);
                    inicio = System.currentTimeMillis();
                    if (rdaahc.isChecked())
                        descargarPorAAHC(url);
                    else {
                        if (rdjava.isChecked())
                            tipo = JAVA;
                        if (rdvolley.isChecked())
                            tipo = VOLLEY;
                        tarea.execute(tipo, url);
                    }
                } catch (Exception e) {
                    Toast.makeText(Ejercicio4Activity.this, "Debes introducir una URL valida", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void descargarPorAAHC(String url)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {

                    private ProgressDialog progreso;

                    @Override
                    public void onStart() {
                        // called before request is started
                        progreso = new ProgressDialog(Ejercicio4Activity.this);
                        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progreso.setMessage("Conectando . . .");
                        progreso.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        // called when response HTTP status is "200 OK"
                        progreso.dismiss();
                        fin = System.currentTimeMillis();
                        paginaWeb.loadData(response, "text/html", "UTF-8");
                        txvDuracion.setText("Duracion: " + String.valueOf(fin - inicio)
                                + " milisegundos");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        progreso.dismiss();
                        Toast.makeText(Ejercicio4Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public class TareaAsincrona extends AsyncTask<String, Integer, String>
    {
        private ProgressDialog progreso;

        private Context context;

        public TareaAsincrona(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            progreso = new ProgressDialog(context);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Conectando . . .");
            progreso.setCancelable(true);
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    TareaAsincrona.this.cancel(true);
                }
            });
            progreso.show();
        }

        protected void onPostExecute(String result)
        {
            progreso.dismiss();
            fin = System.currentTimeMillis();
            paginaWeb.loadDataWithBaseURL(url, pagina, "text/html", "UTF-8", null);
            txvDuracion.setText("Duracion: " + String.valueOf(fin - inicio)
                    + " milisegundos");
        }

        protected String doInBackground(String... cadena)
        {
            try {
                if (cadena[0].equals(JAVA))
                    for (int i = 0; i < 4; i++) {
                        publishProgress(i);
                        pagina = Conexion.conectarJava(cadena[1]);
                    }
                else
                    pagina = conectarVolley(cadena[1]);
            } catch (Exception e) {
                Log.e("HTTP", e.getMessage(), e);
                pagina = null;
                cancel(true);
            }
            return pagina;
        }
        protected void onProgressUpdate(Integer... progress)
        {
            progreso.setMessage("Conectando " + Integer.toString(progress[0]));
        }

        protected void onCancelled() {
            progreso.dismiss();
            paginaWeb.loadDataWithBaseURL(null,url+" cancelado", "text/html", "UTF-8",
                    null);
            txvDuracion.setText("Cancelado");
        }
    }

    public String conectarVolley(String url) {
        // Instantiate the RequestQueue.
        OkHttpClient myOkHttpClient = new OkHttpClient();
        OkHttp3Stack myOkHttp3Stack = new OkHttp3Stack(myOkHttpClient);
        RequestQueue queue = Volley.newRequestQueue(this, myOkHttp3Stack);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void
            onResponse(String response) {
                resultado = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = "";
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    message = "Timeout Error " + error.getMessage();
                }
                else if (error instanceof AuthFailureError) {
                    message = "AuthFailure Error " + error.getMessage();
                }
                else if (error instanceof ServerError) {
                    message = "Server Error " + error.getMessage();
                }
                else if (error instanceof NetworkError) {
                    message = "Network Error " + error.getMessage();
                }
                else if (error instanceof ParseError) {
                    message = "Parse Error " + error.getMessage();
                }
                resultado = message;
            }
        });
        // Set the tag on the request.
        stringRequest.setTag(TAG);
        // Set retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, RETRY, MULTIPLE));
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
        return resultado;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }
}
