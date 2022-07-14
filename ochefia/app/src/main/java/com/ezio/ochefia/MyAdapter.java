package com.ezio.ochefia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private MostraCadastros cadastro;
    private List<Model> mList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Context context;

    public MyAdapter(MostraCadastros cadastro, List<Model> mList){
        this.cadastro = cadastro;
        this.mList = mList;
    }

    public void updateData(int position){
        Model item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("uID", item.getId());
        bundle.putString("uNome", item.getNome());
        bundle.putString("uPreco", item.getPreco());
        bundle.putString("uDesc", item.getDesc());
        Intent intent = new Intent(cadastro, CadastroItem.class);
        intent.putExtras(bundle);
        cadastro.startActivity(intent);
    }

    public void deleteData(int position){
        Model item = mList.get(position);
    db.collection("Documentos").document(item.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                notifyItemRemoved(position);
                Toast.makeText(cadastro, "Produto Deletado", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(cadastro, "Erro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    });
    }

    private void NotifyRemoved(int position){
        mList.remove(position);
        notifyItemRemoved(position);
        cadastro.showData();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(cadastro).inflate(R.layout.itens, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(cadastro).load(mList.get(position).getImageUrl()).into(holder.foto);
        holder.nome.setText(mList.get(position).getNome());
        holder.preco.setText(mList.get(position).getPreco());
        holder.descricao.setText(mList.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView foto;
        TextView nome, preco, descricao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = (ImageView) itemView.findViewById(R.id.foto_text);
            nome = itemView.findViewById(R.id.nome_text);
            preco = itemView.findViewById(R.id.preco_text);
            descricao = itemView.findViewById(R.id.desc_text);


        }
    }
}
