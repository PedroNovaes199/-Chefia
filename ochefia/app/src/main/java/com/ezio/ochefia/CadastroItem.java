package com.ezio.ochefia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ezio.ochefia.databinding.ActivityCadastroItemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class CadastroItem extends AppCompatActivity {

    ActivityCadastroItemBinding binding;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    private FirebaseFirestore db;

    EditText campoNome, campoPreco, campoDescricao;

    private String uId, uNome, uPreco, uDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        campoNome = (EditText)findViewById(R.id.campoNome);
        campoPreco = (EditText)findViewById(R.id.campoPreco);
        campoDescricao = (EditText)findViewById(R.id.campoDescricao);

        //Setando tipo de input
        campoPreco.setInputType(InputType.TYPE_CLASS_NUMBER);

        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            binding.btnSalvar.setText("Atualizar");
            uId = bundle.getString("uID");
            uNome = bundle.getString("uNome");
            uPreco = bundle.getString("uPreco");
            uDesc = bundle.getString("uDesc");
            campoNome.setText(uNome);
            campoPreco.setText(uPreco);
            campoDescricao.setText(uDesc);
        }else{
            binding.btnSalvar.setText("Salvar");
        }

        binding.imgProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        binding.btnSalvar.setOnClickListener(new View.OnClickListener() { /*Salvar*/
            @Override
            public void onClick(View view) {
                uploadImage();

                String nome = campoNome.getText().toString();
                String preco = campoPreco.getText().toString();
                Float.parseFloat(preco);
                String descricao = campoDescricao.getText().toString();

                Bundle bundle1 = getIntent().getExtras();
                if(bundle1 !=null){
                    String id = uId;
                    updateToFireStore(id, nome, preco, descricao);
                }else {
                    String id = UUID.randomUUID().toString();
                    saveToFireStore(id, nome, preco, descricao);
                }
            }
        });
    }

    private void saveToFireStore(String id, String nome, String preco, String descricao){
        if(!nome.isEmpty() && !preco.isEmpty() && !descricao.isEmpty()){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("nome", nome);
            map.put("preco", preco);
            map.put("descricao", descricao);

            db.collection("Documentos").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(CadastroItem.this, "Produto Cadastrado!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CadastroItem.this, "Algum erro aconteceu!", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "Por favor preencha os campos!", Toast.LENGTH_SHORT).show();
        }


    }

    private void updateToFireStore(String id, String nome, String preco, String descricao){
        db.collection("Documentos").document(id).update("nome", nome, "preco", preco, "descricao", descricao)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CadastroItem.this, "Produto Atualizado!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CadastroItem.this, "Algum erro aconteceu!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CadastroItem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Enviando foto....");
        progressDialog.show();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                binding.imgProduto.setImageURI(null);
                Toast.makeText(CadastroItem.this, "Imagem enviada com sucesso!",Toast.LENGTH_SHORT).show();

                if(progressDialog.isShowing())progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(progressDialog.isShowing())progressDialog.dismiss();
                Toast.makeText(CadastroItem.this, "Imagem não enviada!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            binding.imgProduto.setImageURI(imageUri);
        }
    }
}