package com.example.appveterinaria;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

public class Buscar extends AppCompatActivity {

    EditText edtIdBuscado, edtNombre, edtTipo, edtRaza, edtColor, edtPeso, edtGenero;
    Button btnBuscarMascota, btnActualizarMascota, btnEliminarMascota;
    private final String URL = "http://192.168.18.22:3001/mascotas/";
    RequestQueue requestQueue;

    private void loadUI(){
        edtIdBuscado = findViewById(R.id.edtIdBuscado);
        edtNombre = findViewById(R.id.edtNombreEdit);
        edtTipo = findViewById(R.id.edtTipoEdit);
        edtRaza = findViewById(R.id.edtRazaEdit);
        edtColor = findViewById(R.id.edtColorEdit);
        edtPeso = findViewById(R.id.edtPesoEdit);
        edtGenero = findViewById(R.id.edtGeneroEdit);
        btnBuscarMascota = findViewById(R.id.btnBuscarMascota);
        btnActualizarMascota = findViewById(R.id.btnActualizarMascota);
        btnEliminarMascota = findViewById(R.id.btnEliminarMascota);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            //Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadUI();

        btnBuscarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchById();
            }
        });
        btnActualizarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmUpdate();
            }
        });
        btnEliminarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });
    }//On Create
    private void searchById(){
        String idmascota= edtIdBuscado.getText().toString().trim();

        if (idmascota.isEmpty()){
            edtIdBuscado.setError("Escriba el ID");
            edtIdBuscado.requestFocus();
        }else{
            //1. Canal de comunicacion
            requestQueue = Volley.newRequestQueue(this);
            String endPoint = URL + idmascota;
            //2. Solicitud
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    endPoint,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("Respuesta WS:", jsonObject.toString()); // <-- AÑADIDO
                            try{
                                edtNombre.setText(jsonObject.getString("nombre"));
                                edtTipo.setText(jsonObject.getString("tipo"));
                                edtRaza.setText(jsonObject.getString("raza"));
                                edtColor.setText(jsonObject.getString("color"));
                                double pesoDouble = jsonObject.getDouble("peso");
                                edtPeso.setText(String.valueOf(pesoDouble));
                                edtGenero.setText(jsonObject.getString("genero"));

                            }catch(JSONException e){
                                Log.e("Error JSON", e.toString()); // <-- AÑADIDO
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e("Error WS", volleyError.toString()); // <-- AÑADIDO
                            formClear();
                            edtIdBuscado.requestFocus();
                            Toast.makeText(getApplicationContext(), "No existe la Mascota", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            //3.Envio de la solicitud
            requestQueue.add(jsonObjectRequest);
        }
    }
    private void updateMascotas(){
        //1. canal de Comunicacion
        requestQueue = Volley.newRequestQueue(this);
        //2. Json a Enviar (BODY)
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("nombre", edtNombre.getText().toString().trim());
            jsonObject.put("tipo", edtTipo.getText().toString().trim());
            jsonObject.put("raza", edtRaza.getText().toString().trim());
            jsonObject.put("color", edtColor.getText().toString().trim());
            String pesoText = edtPeso.getText().toString().trim();
            double pesoDouble = Double.parseDouble(pesoText);
            jsonObject.put("peso", pesoDouble);
            jsonObject.put("genero", edtGenero.getText().toString().trim());
        }catch (JSONException e){
            Log.e("Error JSON", e.toString());
        }

        //3. Solicitud(Utilizara el JSON del paso 2)
        String endPoint = URL + edtIdBuscado.getText().toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                endPoint,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(getApplicationContext(), "Actualizado Correctamente", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "No se pudo Actualizar", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        //4. Enviamos la solicitud
        requestQueue.add(jsonObjectRequest);
    }
    private void deleteMascota(){
        requestQueue = Volley.newRequestQueue(this);
        String endPoint = URL + edtIdBuscado.getText().toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                endPoint,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        formClear();
                        Toast.makeText(getApplicationContext(), "Eliminacion Exitosa", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "No se pudo Eliminar", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
    private void formClear(){
        edtNombre.setText(null);
        edtTipo.setText(null);
        edtRaza.setText(null);
        edtColor.setText(null);
        edtPeso.setText(null);
        edtGenero.setText(null);
    }
    private void confirmUpdate(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Actualizacion de Mascotas");
        dialog.setMessage("¿Procedemos con la Actualizacion?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("Cancelar", null);
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateMascotas();
            }
        });
        dialog.show();
    }
    private void confirmDelete(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Eliminacion de la Mascota");
        dialog.setMessage("¿Procedemos con la Eliminacion?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("Cancelar", null);
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteMascota();
            }
        });
        dialog.show();
    }
}