package com.example.pantallaslistados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pantallaslistados.Adaptadores.Adaptador;
import com.example.pantallaslistados.Objetos.Alumno;
import com.example.pantallaslistados.Objetos.Catedratico;
import com.example.pantallaslistados.Objetos.FotografiaUsuario;
import com.example.pantallaslistados.Objetos.Grupo;
import com.example.pantallaslistados.Objetos.RestApiMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InfoGrupo extends AppCompatActivity {

    String idGrupo;
    Catedratico catedratico;
    TextView txtCateNombreInfo, txtCorreoInfo, txtTelInfo;
    ImageView imgCatedratico;

    ArrayList<FotografiaUsuario> listadoFotos;
    ListView listadoIntegrantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_grupo);

        Intent intent = getIntent();
        idGrupo = intent.getStringExtra("idGrupo");
        String nombreGrupo = intent.getStringExtra("nombreGrupo");
        String codigoGrupo = intent.getStringExtra("codigoGrupo");

        TextView txtNombreInfo = (TextView) findViewById(R.id.txtNombreInfo);
        TextView txtCodigoInfo = (TextView) findViewById(R.id.txtCodigoInfo);
        txtCateNombreInfo = (TextView) findViewById(R.id.txtCateNombreInfo);
        txtCorreoInfo = (TextView) findViewById(R.id.txtCorreoInfo);
        txtTelInfo = (TextView) findViewById(R.id.txtTelInfo);
        imgCatedratico = (ImageView) findViewById(R.id.imgAdmin);
        listadoIntegrantes = (ListView) findViewById(R.id.listadoAlumnos);

        txtNombreInfo.setText(nombreGrupo);
        txtCodigoInfo.setText(codigoGrupo);

        PostCatedratico();
        listarIntegrantes();

        listadoIntegrantes.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String idIntegrante = listadoFotos.get(position).getId();
                Intent infoIntegrante = new Intent(getApplicationContext(), MainActivity.class /*activity de Karol*/);
                infoIntegrante.putExtra("idIntegrante", idIntegrante);
                startActivity(infoIntegrante);
            }
        });

    }

    private void PostCatedratico(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = RestApiMethods.ApiPostAdmin;
        JSONObject postData = new JSONObject();
        try {
            postData.put("grupoid", idGrupo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equalsIgnoreCase("1")) {
                        JSONArray grupoArrayJSON = response.getJSONArray("data");

                        for (int i = 0; i < grupoArrayJSON.length(); i++) {
                            JSONObject grupoObject = grupoArrayJSON.getJSONObject(i);

                            catedratico = new Catedratico(grupoObject.getString("CATEDRATICO"),
                                    grupoObject.getString("TELEFONO"),
                                    grupoObject.getString("FOTO_URL"),
                                    grupoObject.getString("CORREO"));
                        }
                        txtCateNombreInfo.setText(catedratico.getNombre());
                        txtCorreoInfo.setText(catedratico.getCorreo());
                        txtTelInfo.setText(catedratico.getTelefono());

                        if(catedratico.getFoto().equalsIgnoreCase("null") == false) {
                            byte[] foto = Base64.decode(catedratico.getFoto().getBytes(), Base64.DEFAULT);
                            Bitmap bitMapFoto = BitmapFactory.decodeByteArray(foto, 0, foto.length);
                            imgCatedratico.setImageBitmap(bitMapFoto);
                        }else {
                            imgCatedratico.setImageResource(R.drawable.img_default);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException ex) {
                    Toast.makeText(getApplicationContext(), "Excepción en Response", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error en Response", "onResponse: " +  error.getMessage().toString() );
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void listarIntegrantes(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = RestApiMethods.ApiPostMembers;
        JSONObject postData = new JSONObject();
        try {
            postData.put("grupoid", idGrupo);
            postData.put("token", "779f16aa3ec7e6b4fd52f852edcef89ad79d8ad9");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                listadoFotos = new ArrayList<FotografiaUsuario>();
                try {
                    if(response.getString("status").equalsIgnoreCase("1")) {
                        JSONArray grupoArrayJSON = response.getJSONArray("data");

                        for (int i = 0; i < grupoArrayJSON.length(); i++) {
                            JSONObject grupoObject = grupoArrayJSON.getJSONObject(i);
                            Alumno alumno = new Alumno();
                            alumno.setId(grupoObject.getString("USUARIOID"));
                            alumno.setNombres(grupoObject.getString("ALUMNO"));
                            alumno.setFoto(grupoObject.getString("FOTO"));

                            Bitmap bitmap;
                            if(alumno.getFoto().equalsIgnoreCase("null") == false) {
                                byte[] foto = Base64.decode(alumno.getFoto().getBytes(), Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(foto, 0, foto.length);
                            }else {
                                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_default);
                            }
                            FotografiaUsuario fotografia = new FotografiaUsuario(alumno.getId(), bitmap, alumno.getNombres());
                            listadoFotos.add(fotografia);
                        }

                        Adaptador adp = new Adaptador(getApplicationContext(), R.layout.imgtxt_view_items, listadoFotos );
                        listadoIntegrantes.setAdapter(adp);

                    }else{
                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException ex) {
                    Toast.makeText(getApplicationContext(), "Excepción en Response", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error en Response", "onResponse: " +  error.getMessage().toString() );
            }
        });
        queue.add(jsonObjectRequest);
    }

}