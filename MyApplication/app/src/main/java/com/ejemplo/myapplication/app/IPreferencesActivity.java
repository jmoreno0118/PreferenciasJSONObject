package com.ejemplo.myapplication.app;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class IPreferencesActivity extends ActionBarActivity {
    EditText nombre, telefono, hora;
    TextView tnombre, ttelefono, thora, tactivo;
    ToggleButton activo;
    ProgressBar barra;

    String NOMBRE = "datos_usuario2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipreferences);

        nombre = (EditText) findViewById(R.id.edtNombre);
        telefono = (EditText) findViewById(R.id.edtTelefono);
        hora = (EditText) findViewById(R.id.edtHora);
        activo = (ToggleButton) findViewById(R.id.toggleActive);
        barra = (ProgressBar) findViewById(R.id.progressBar);
        tnombre = (TextView) findViewById(R.id.textView);
        ttelefono = (TextView) findViewById(R.id.textView2);
        thora = (TextView) findViewById(R.id.textView3);
        tactivo = (TextView) findViewById(R.id.textView4);

        nombre.setVisibility(View.INVISIBLE);
        telefono.setVisibility(View.INVISIBLE);
        hora.setVisibility(View.INVISIBLE);
        activo.setVisibility(View.INVISIBLE);
        tnombre.setVisibility(View.INVISIBLE);
        ttelefono.setVisibility(View.INVISIBLE);
        thora.setVisibility(View.INVISIBLE);
        tactivo.setVisibility(View.INVISIBLE);

        barra.setVisibility(View.VISIBLE);
        obtenerTextoInternet();
    }


    private void obtenerTextoInternet(){
        if(isNetworkAvailable()){
            GetAPI getAPI = new GetAPI();
            getAPI.execute();
        }else{
            leerMemoriaInterna();
        }
    }

    private boolean isNetworkAvailable(){
        boolean isAvailable = false;
        try{
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()){
                isAvailable = true;
            }else{
                Toast.makeText(this, "Sin Conexion", Toast.LENGTH_LONG);
            }
        } catch(Exception e){

        }
        return isAvailable;
    }

    private class GetAPI extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object ... objects){
            int responseCode = -1;
            String resultado = "";
            try{
                URL apiURL = new URL("http://continentalrescueafrica.com/2013/test.php");
                HttpURLConnection httpConnection = (HttpURLConnection)apiURL.openConnection();
                httpConnection.connect();

                responseCode = httpConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = httpConnection.getInputStream();
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8);
                    StringBuilder sBuilder = new StringBuilder();

                    String line = null;
                    while((line = bReader.readLine()) != null){
                        sBuilder.append(line + "\n");
                    }
                    inputStream.close();
                    resultado = sBuilder.toString();
                }
            }catch(MalformedURLException e){}
            catch(IOException e){}
            catch(Exception e){}

            return resultado;
        }
        @Override
        protected  void onPostExecute(String s){
            try{
                guardarMemoriaInterna(s);
                setInfo(s);
            }catch(Exception e){}
        }
    }

    public void guardarMemoriaInterna(String s){
        try{
            FileOutputStream fileOutputStream = openFileOutput(NOMBRE, Context.MODE_PRIVATE);
            fileOutputStream.write(s.getBytes());
            fileOutputStream.close();
        }catch(Exception e){}
    }

    public void setInfo(String s){
        try{
            nombre.setVisibility(View.VISIBLE);
            telefono.setVisibility(View.VISIBLE);
            hora.setVisibility(View.VISIBLE);
            activo.setVisibility(View.VISIBLE);
            tnombre.setVisibility(View.VISIBLE);
            ttelefono.setVisibility(View.VISIBLE);
            thora.setVisibility(View.VISIBLE);
            tactivo.setVisibility(View.VISIBLE);

            barra.setVisibility(View.INVISIBLE);

            JSONObject json = new JSONObject(s);
            nombre.setText(json.getString("nombre"));
            telefono.setText(json.getString("telefono"));
            hora.setText(json.getString("hora"));
            boolean active = Boolean.parseBoolean(json.getString("activo"));
            activo.setChecked(active);
        }catch(Exception e){}
    }

    public void leerMemoriaInterna(){
        try{
            String textoMemoria = "";
            FileInputStream fileInputStream = openFileInput(NOMBRE);

            BufferedReader bReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"),8);
            StringBuilder sBuilder = new StringBuilder();
            String line = null;
            while((line = bReader.readLine()) != null){
                sBuilder.append(line);
            }
            textoMemoria = sBuilder.toString();
            setInfo(textoMemoria);
        }catch(Exception e){
            noUser();
        }
    }

    public void noUser(){
        nombre.setVisibility(View.INVISIBLE);
        telefono.setVisibility(View.INVISIBLE);
        hora.setVisibility(View.INVISIBLE);
        activo.setVisibility(View.INVISIBLE);
        tnombre.setVisibility(View.VISIBLE);
        ttelefono.setVisibility(View.INVISIBLE);
        thora.setVisibility(View.INVISIBLE);
        tactivo.setVisibility(View.INVISIBLE);

        barra.setVisibility(View.INVISIBLE);
        tnombre.setText("Usuario sin datos");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ipreferences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
