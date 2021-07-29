package com.example.pantallaslistados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pantallaslistados.Objetos.Grupo;
import com.example.pantallaslistados.Objetos.RestApiMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listadoGrupos;
    ArrayList<Grupo> arregloGrupos;
    ArrayList<String> arregloNombresGrupos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listadoGrupos = (ListView) findViewById(R.id.listadoCatedraticos);
        listarGruposUsuario(/*token*/); //AL MOMENTO DE UTILIZAR EL VERDADERO TOKEN DEBE PASARSE COMO PARAMETRO

        listadoGrupos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String idGrupo = arregloGrupos.get(position).getId();
                String nombreGrupo = arregloGrupos.get(position).getNombre();
                String codigoGrupo = arregloGrupos.get(position).getCodigo();
                Intent grupoEspecifico = new Intent(getApplicationContext(), ArchivosGrupo.class);
                grupoEspecifico.putExtra("idGrupo", idGrupo);
                grupoEspecifico.putExtra("nombreGrupo", nombreGrupo);
                grupoEspecifico.putExtra("codigoGrupo", codigoGrupo);
                startActivity(grupoEspecifico);
            }
        });
    }

    private void listarGruposUsuario(/*String token*/){
        arregloGrupos = new ArrayList<Grupo>();
        arregloNombresGrupos = new ArrayList<String>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = RestApiMethods.ApiGrupsUser;
        JSONObject postData = new JSONObject();
        try {
            postData.put("token", "779f16aa3ec7e6b4fd52f852edcef89ad79d8ad9");
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

                            Grupo grupo = new Grupo(grupoObject.getString("GRUPOID"),
                                    grupoObject.getString("GRUPO"),
                                    grupoObject.getString("CODIGO"),
                                    grupoObject.getString("USUARIOID"));

                            arregloGrupos.add(grupo);
                            arregloNombresGrupos.add(grupo.getNombre());
                        }
                        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arregloNombresGrupos);
                        listadoGrupos.setAdapter(adp);
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