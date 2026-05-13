package model;
import java.time.LocalDate;

public class Entrega {
    //1. Atributos (As informações que a ficha vai guardar)
    private LocalDate data;
    private int sucessos;
    private int falhas;

    // 2. Construtor (O botão que "imprime" uma ficha nova preenchida)
    public Entrega(LocalDate data, int sucessos, int falhas) {
        validarValores(sucessos,falhas);
        this.data = data;
        this.sucessos = sucessos;
        this.falhas = falhas;
    }

    private void validarValores(int sucessos, int falhas){
        if (sucessos < 0 || falhas < 0) {
            throw new IllegalArgumentException("Os valores de pacotes não podem ser negativos.");
        }
    }
    
    public LocalDate getData(){
        return this.data;
    }
    public int getSucessos(){
        return sucessos;
    }
    public int getFalhas (){
        return falhas;
    }
    // 3. Método (A inteligência da ficha: sabe calcular o total do dia)
    public int calcularTotalDoDia(){
        return this.sucessos + this.falhas;
    }

}
