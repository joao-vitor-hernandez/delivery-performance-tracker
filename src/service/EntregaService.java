package service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import model.Entrega;
import repository.EntregaRepository;

public class EntregaService {
    private final EntregaRepository repository;

    public EntregaService(EntregaRepository repository){
        this.repository = repository;
    }

    public List<Entrega> obterEntregasDoMesAtual(){
        List<Entrega> todas = repository.buscarTodas();
        LocalDate hoje = LocalDate.now();

        return todas.stream().filter(e -> e.getData().getMonthValue() == hoje.getMonthValue() && e.getData().getYear() == hoje.getYear()).collect(Collectors.toList());
    }
    private int[] calcularTotais(List<Entrega> entregas){
        int sucessos = entregas.stream().mapToInt(Entrega::getSucessos).sum();
        int falhas = entregas.stream().mapToInt(Entrega::getFalhas).sum();
        return new int[]{sucessos,falhas};
    }

    public double calcularTaxaSucesso(List<Entrega> entregas){
        int[] totais = calcularTotais(entregas);
        int totalSucesso = totais[0];
        int totalGeral = totais[0] + totais[1];

        if (totalGeral == 0) return 0;
        return((double)totalSucesso/totalGeral)*100;
    }

    public int calcularProjecaoPlatina(List<Entrega> entregas, double metaDesejada){
        int[] totais = calcularTotais(entregas);
        int totalSucesso = totais[0];
        int totalGeral = totais[0] + totais[1];

        //proteção para caso a meta seja 100% a conta não seja divida por zero
        if (metaDesejada >= 1.0) {
            return totais[1];
        }

        double faltam = (metaDesejada*totalGeral-totalSucesso)/(1-metaDesejada);
        return Math.max(0,(int) Math.ceil(faltam));
    }
}