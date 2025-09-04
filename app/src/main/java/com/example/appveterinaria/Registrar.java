package com.example.appveterinaria;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Registrar extends AppCompatActivity {

    EditText edtNombre, edtTipo, edtRaza, edtColor, edtPeso, edtGenero;
    Button btnGuardar;
    private final String URL = "http://192.168.101.31:3001/mascotas";
    RequestQueue requestQueue;

    private void loadUI(){
        edtNombre = findViewById(R.id.edtNombre);
        edtTipo = findViewById(R.id.edtTipo);
        edtRaza = findViewById(R.id.edtRaza);
        edtColor = findViewById(R.id.edtColor);
        edtPeso = findViewById(R.id.edtPeso);
        edtGenero = findViewById(R.id.edtGenero);
        btnGuardar = findViewById(R.id.btnGuardar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadUI();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendDataWS();
                if (formIsReady()){
                    showConfirmSave();
                }else{
                    edtNombre.requestFocus();
                }
            }
        });
    }

    private boolean editTextValidate(EditText editText, String message){
        if (editText.getText().toString().trim().isEmpty()){
            editText.setError(message);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private boolean editTextValidate(EditText editText){
        if (editText.getText().toString().trim().isEmpty()){
            editText.setError("Campo Obligatorio");
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private boolean formIsReady(){
        if (    !editTextValidate(edtNombre, "Nombre Requerido") ||
                !editTextValidate(edtTipo) ||
                !editTextValidate(edtRaza) ||
                !editTextValidate(edtColor) ||
                !editTextValidate(edtPeso) ||
                !editTextValidate(edtGenero)){
            return false;
        }

        return true;
    }

    private void showConfirmSave(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Registro de Mascotas");
        dialog.setMessage("¿Esta seguro de registrar a esta Mascota?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("Cancelar", null);
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendDataWS();
            }
        });
        dialog.show();
    }
    private void sendDataWS(){
        requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("nombre", edtNombre.getText().toString());
            jsonObject.put("tipo", edtTipo.getText().toString());
            jsonObject.put("raza", edtRaza.getText().toString());
            jsonObject.put("color", edtColor.getText().toString());
            jsonObject.put("peso", Double.parseDouble(edtPeso.getText().toString()));
            jsonObject.put("genero", edtGenero.getText().toString());
        }catch (Exception error){
            Log.e("Error JSON envio:", error.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("ID obtenido:", jsonObject.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}