package com.entregas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "tb_usuario") // Define o nome correto da tabela de motoristas
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento gerenciado pelo banco
    private Long id;

    @Column(unique = true, nullable = false) // Impede usernames duplicados ou nulos
    private String username;

    @Column(nullable = false)
    private String senhaHash;

    // 1. Construtor padrão obrigatório pelo JPA/Hibernate
    protected Usuario() {
    }

    // 2. Construtor completo adaptado para Long
    public Usuario(Long id, String username, String senhaHash){
        this.id = id;
        this.username = username;
        this.senhaHash = senhaHash;
    }

    // 3. Construtor simples (Usado no cadastro de novos motoristas)
    public Usuario(String username, String senhaHash){
        this.username = username;
        this.senhaHash = senhaHash;
    }

    // Getters e Setters adaptados para o tipo Long
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }
}