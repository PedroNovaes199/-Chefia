package Modelo;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Produtos {

    private String uid;
    private String nome;
    private float preco;
    private String descricao;

    public Produtos(String uid, String nome, float preco, String descricao) {
        this.uid = uid;
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return nome;
    }
}
