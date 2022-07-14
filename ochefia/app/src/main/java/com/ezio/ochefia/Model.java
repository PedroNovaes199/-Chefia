package com.ezio.ochefia;

import android.net.Uri;

public class Model {

    String id, imageUrl, nome, preco, desc;
    public Model(){}



    public Model(String id, String imageUrl, String nome, String preco, String desc){
            this.id = id;
            this.imageUrl = imageUrl;
            this.nome = nome;
            this.preco = preco;
            this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
