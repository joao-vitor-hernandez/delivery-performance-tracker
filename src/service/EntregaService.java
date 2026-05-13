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
}
