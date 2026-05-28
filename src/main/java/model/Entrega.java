package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDate;

@Entity
@Table(name= "tb_entrega")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private int usuarioId;

    private LocalDate data;
    private int sucessos;
    private int falhas;

    public Entrega(){}
    // 2. Construtor completo (O botão que "imprime" uma ficha nova preenchida)
    public Entrega(Long id, int usuarioId, LocalDate data, int sucessos, int falhas) {
        validarValores(sucessos,falhas);
        this.id = id;
        this.usuarioId = usuarioId;
        this.data = data;
        this.sucessos = sucessos;
        this.falhas = falhas;
    }

    //3. Construtor simples
    public Entrega(int usuarioId, LocalDate data, int sucessos, int falhas){
        validarValores(sucessos, falhas);
        this.usuarioId = usuarioId;
        this.data = data;
        this.sucessos = sucessos;
        this.falhas = falhas;
    }

    private void validarValores(int sucessos, int falhas){
        if (sucessos < 0 || falhas < 0) {
            throw new IllegalArgumentException("Os valores de pacotes não podem ser negativos.");
        }
    }
    //getters e setters
    public Long getId(){ return id;}
    public void setId(Long id) {this.id = id;}

    public int getUsuarioId() {return usuarioId;}
    public void setUsuarioId(int usuarioId) {this.usuarioId = usuarioId;}

    public LocalDate getData() {return this.data;}
    public void setData(LocalDate data) {this.data = data;}

    public int getSucessos() {return sucessos;}
    public void setSucessos(int sucessos) {this.sucessos = sucessos;}

    public int getFalhas () {return falhas;}
    public void setFalhas(int falhas) {this.falhas = falhas;}

    // 4. Método (A inteligência da ficha: sabe calcular o total do dia)
    public int calcularTotalDoDia(){
        return this.sucessos + this.falhas;
    }

}
