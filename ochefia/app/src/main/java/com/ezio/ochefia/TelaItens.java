package com.ezio.ochefia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TelaItens extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_itens);

        Button bt_cadastro = this.findViewById(R.id.button);
        Button bt_editar = this.findViewById(R.id.editar);

        bt_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaItens.this, CadastroItem.class);
                startActivity(intent);
            }
        });

        bt_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaItens.this, MostraCadastros.class);
                startActivity(intent);
            }
        });
    }
}