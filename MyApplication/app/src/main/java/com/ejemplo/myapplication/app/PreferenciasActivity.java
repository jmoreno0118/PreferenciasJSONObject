package com.ejemplo.myapplication.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class PreferenciasActivity extends ActionBarActivity {
    ToggleButton toobleButton;
    Button button,button2;
    EditText editText;
    CheckBox checkBox;
    String NOMBRE = "hola_mundo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        toobleButton = (ToggleButton) findViewById(R.id.toggleButton);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        editText = (EditText) findViewById(R.id.editText);
        checkBox = (CheckBox) findViewById(R.id.checkPref);

        SharedPreferences sharedPreferences;
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        boolean opcion1;
        boolean opcion2;
        String opcion3;

        opcion1 = sharedPreferences.getBoolean("Opcion 1", false);
        opcion2 = sharedPreferences.getBoolean("Opcion2", false);
        opcion3 = sharedPreferences.getString("Opcion3","Hello");

        toobleButton.setChecked(opcion1);
        checkBox.setChecked(opcion2);
        editText.setText(opcion3);
    }

    public void guardarPreferencias (View view){
        SharedPreferences sharedPreferences;
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("Opcion1", toobleButton.isChecked());
        editor.putBoolean("Opcion2", checkBox.isChecked());
        editor.putString("Opcion3",editText.getText().toString());

        editor.commit();
    }

    public void guardarMemoriaInterna(View view){
        try{
            String texto = editText.getText().toString();
            FileOutputStream fileOutputStream = openFileOutput(NOMBRE, Context.MODE_PRIVATE);
            fileOutputStream.write(texto.getBytes());
            fileOutputStream.close();
        }catch(Exception e){}
    }

    public void leerMemoriaInterna(View view){
        try{
            String textoMemoria;
            FileInputStream fileInputStream = openFileInput(NOMBRE);

            BufferedReader bReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"),8);
            StringBuilder sBuilder = new StringBuilder();
            String line = null;
            while((line = bReader.readLine()) != null){
                sBuilder.append(line);
            }
            textoMemoria = sBuilder.toString();
            editText.setText(textoMemoria);
        }catch(Exception e){}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.preferencias, menu);
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
