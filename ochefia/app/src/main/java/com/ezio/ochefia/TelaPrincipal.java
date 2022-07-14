package com.ezio.ochefia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class TelaPrincipal extends AppCompatActivity {

    private TextView nomeUsuario, emailUsuario;
    private Button bt_deslogar;
    private Button bt_gerencia;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        getSupportActionBar().hide();
        IniciarComponents();

        bt_deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(TelaPrincipal.this,FormLogin.class);
                startActivity(intent);
                finish();
            }
        });

        bt_gerencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent serve para chamar outras classes
                Intent intent = new Intent(TelaPrincipal.this, TelaItens.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null) {
                    nomeUsuario.setText(documentSnapshot.getString("nome"));
                    emailUsuario.setText(email);
                }
            }
        });
    }

    private void IniciarComponents() {
        nomeUsuario = findViewById(R.id.textNomeUser);
        emailUsuario = findViewById(R.id.texttEmailUser);
        bt_deslogar = findViewById(R.id.bt_deslogar);
        bt_gerencia = findViewById(R.id.bt_gerencia);
    }
}