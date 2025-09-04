package com.example.appveterinaria;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
    private final String URL = "http://192.168.101.31:3001/mascotas/";
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
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadUI();

        btnBuscarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchById();
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
            String enPoint = URL + idmascota;
            //2. Solicitud
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    enPoint,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            //Log.d("Respuesta WS:", jsonObject.toString());
                            try{
                                edtNombre.setText(jsonObject.getString("nombre"));
                                edtTipo.setText(jsonObject.getString("tipo"));
                                edtRaza.setText(jsonObject.getString("raza"));
                                edtColor.setText(jsonObject.getString("color"));
                                double pesoDouble = jsonObject.getDouble("peso");
                                edtPeso.setText(String.valueOf(pesoDouble));
                                edtGenero.setText(jsonObject.getString("genero"));

                            }catch(JSONException e){
                                Log.e("Error JSON", e.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Log.e("Error WS", volleyError.toString());
                            formClear();
                            edtIdBuscado.requestFocus();
                            Toast.makeText(getApplicationContext(), "No existe el Vehiculo", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            //3.Envio de la solicitud
            requestQueue.add(jsonObjectRequest);
        }
    }
    private void formClear(){
        edtNombre.setText(null);
        edtTipo.setText(null);
        edtRaza.setText(null);
        edtColor.setText(null);
        edtPeso.setText(null);
        edtGenero.setText(null);
    }

}