package model;

public class Usuario {
    private int id;
    private String username;
    private String senhaHash;

    //construtor completo
    public Usuario(int id, String username, String senhaHash){
        this.id = id;
        this.username = username;
        this.senhaHash = senhaHash;
    }
    //construtor simples
    public Usuario(String username, String senhaHash){
        this.username = username;
        this.senhaHash = senhaHash;
    }

    //getters e setters (leitura escrita)
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getSenhaHash() {return senhaHash;}
    public void setSenhaHash(String senhaHash) {this.senhaHash = senhaHash;}
}
