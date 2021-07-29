package com.example.pantallaslistados;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ArchivosGrupo extends AppCompatActivity {

    String idGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivos_grupo);

        Intent intent = getIntent();
        idGrupo = intent.getStringExtra("idGrupo");
        String nombreGrupo = intent.getStringExtra("nombreGrupo");
        String codigoGrupo = intent.getStringExtra("codigoGrupo");

        TextView tituloClase = (TextView) findViewById(R.id.tituloClase);
        ImageButton btnInfo = (ImageButton) findViewById(R.id.btnInfo);
        tituloClase.setText(nombreGrupo);

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idGrupo != null) {
                    Intent pantallaInfo = new Intent(getApplicationContext(), InfoGrupo.class);
                    pantallaInfo.putExtra("idGrupo", idGrupo);
                    pantallaInfo.putExtra("nombreGrupo", nombreGrupo);
                    pantallaInfo.putExtra("codigoGrupo", codigoGrupo);
                    startActivity(pantallaInfo);
                }
            }
        });
    }
}