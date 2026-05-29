package service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import model.Entrega;
import repository.EntregaRepository;
import org.springframework.stereotype.Service;

@Service
public class EntregaService {
    private final EntregaRepository repository;

    public EntregaService(EntregaRepository repository){
        this.repository = repository;
    }

    public List<Entrega> obterEntregasDoMesAtual(Long usuarioId){
        LocalDate hoje = LocalDate.now();
        int mesAtual = hoje.getMonthValue();
        int anoAtual = hoje.getYear();

        return repository.findByUsuarioId(usuarioId).stream()
                .filter(e -> e.getData().getMonthValue() == mesAtual && e.getData().getYear() == anoAtual)
                .collect(Collectors.toList());
    }
    
    public Entrega salvarOuAtualizar(Entrega entrega){
        Entrega entregaExistente = repository.findByUsuarioIdAndData(entrega.getUsuarioId(), entrega.getData());
        if(entregaExistente != null){
            entregaExistente.setSucessos(entrega.getSucessos());
            entregaExistente.setFalhas(entrega.getFalhas());
            return repository.save(entregaExistente);
        }
        return repository.save(entrega);
    }

    public int getTotalPacotes (List<Entrega> entregas){
        return calcularTotais(entregas)[2];
    }
    
    private int[] calcularTotais(List<Entrega> entregas){
        int sucessos = entregas.stream().mapToInt(Entrega::getSucessos).sum();
        int falhas = entregas.stream().mapToInt(Entrega::getFalhas).sum();
        int geral = sucessos + falhas;
        return new int[]{sucessos,falhas,geral};
    }

    public double calcularTaxaSucesso(List<Entrega> entregas){
        int[] totais = calcularTotais(entregas);
        if (totais[2] == 0) return 0;
        return((double)totais[0]/totais[2])*100;
    }

    public int calcularProjecaoPlatina(List<Entrega> entregas, double metaDesejada){
        int[] totais = calcularTotais(entregas);
        int totalSucesso = totais[0];
        int totalFalha = totais[1];
        int totalGeral = totais[2];

        //proteção para caso a meta seja 100% a conta não seja divida por zero
        if (metaDesejada >= 1.0) {
            return totalFalha;
        }

        double faltam = (metaDesejada*totalGeral-totalSucesso)/(1-metaDesejada);
        return Math.max(0,(int) Math.ceil(faltam));
    }

    public Entrega salvarEntrega(Entrega entrega) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'salvarEntrega'");
    }

    public List<Entrega> buscarPorUsuario(Long usuarioId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorUsuario'");
    }
}