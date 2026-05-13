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

    public double calcularTaxaSucesso(List<Entrega> entregas){
        int totalSucesso = entregas.stream().mapToInt(Entrega::getSucessos).sum();
        int totalFalha = entregas.stream().mapToInt(Entrega::getFalhas).sum();
        int totalGeral = totalSucesso + totalFalha;

        if (totalGeral == 0) return 0;
        return((double)totalSucesso/totalGeral)*100;
    }

    public int calcularProjecaoPlatina(List<Entrega> entregas, double metaDesejada){
        int totalSucesso = entregas.stream().mapToInt(Entrega::getSucessos).sum();
        int totalFalha = entregas.stream().mapToInt(Entrega::getFalhas).sum();
        int totalGeral = totalSucesso + totalFalha;

        double faltam = (metaDesejada*totalGeral-totalSucesso)/(1-metaDesejada);

        return (int) Math.ceil(faltam);
    }
}
