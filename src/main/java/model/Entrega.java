package model;
import java.time.LocalDate;

public class Entrega {
    //1. Atributos (As informações que a ficha vai guardar)
    private int id;
    private int usuarioId;
    private LocalDate data;
    private int sucessos;
    private int falhas;

    // 2. Construtor completo (O botão que "imprime" uma ficha nova preenchida)
    public Entrega(int id, int usuarioId, LocalDate data, int sucessos, int falhas) {
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
    public int getId(){ return id;}
    public void setId(int id) {this.id = id;}

    public int getUsuarioId() {return usuarioId;}
    public void setUsuarioId(int usuarioId) {this.usuarioId = usuarioId;}

    public LocalDate getData() {return this.data;}
    public int getSucessos() {return sucessos;}
    public int getFalhas () {return falhas;}

    // 4. Método (A inteligência da ficha: sabe calcular o total do dia)
    public int calcularTotalDoDia(){
        return this.sucessos + this.falhas;
    }

}
