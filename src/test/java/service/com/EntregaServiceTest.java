package service.com;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.entregas.model.Entrega;
import com.entregas.repository.EntregaRepository;
import com.entregas.service.EntregaService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EntregaServiceTest {

    @Mock
    private EntregaRepository repository;

    @InjectMocks
    private EntregaService entregaService;

    private List<Entrega> entregas;
    private Long usuarioIdExemplo; 

    @BeforeEach
    void setUp() {
        usuarioIdExemplo = 1L; 
        entregas = new ArrayList<>();
    }

    @Test
    @DisplayName("Deve calcular a taxa de sucesso corretamente com uma lista de entregas")
    void deveCalcularTaxaSucessoComSucesso() {
        List<Entrega> entregas = new ArrayList<>();
        Entrega entrega1 = new Entrega(1L, null, LocalDate.now(), 95, 5); 
        entregas.add(entrega1);

        double taxaCalculada = entregaService.calcularTaxaSucesso(entregas);
        assertEquals(95.0, taxaCalculada, 0.001, "A taxa de sucesso para 95 sucessos de 100 totais deve ser 95.0%");
    }

    @Test
    @DisplayName("Deve retornar zero se o total de pacotes na lista for zero")
    void deveTratarDivisaoPorZeroAoCalcularTaxa() {
        List<Entrega> entregas = new ArrayList<>();
        Entrega entregaVazia = new Entrega(1L, null, LocalDate.now(), 0, 0);
        entregas.add(entregaVazia);

        double taxaCalculada = entregaService.calcularTaxaSucesso(entregas);
        assertEquals(0.0, taxaCalculada, "Se não há pacotes movimentados, a taxa deve ser 0 para evitar divisão por zero.");
    }

    @Test
    @DisplayName("STRESS: Deve calcular corretamente a projeção para meta de 98% (Nível Platina)")
    void deveCalcularProjecaoPlatinaComSucesso() {
        List<Entrega> entregas = new ArrayList<>();
        Entrega entregaStress = new Entrega(1L, null,LocalDate.now(), 80, 20);
        entregas.add(entregaStress);

        int pacotesNecessarios = entregaService.calcularProjecaoPlatina(entregas, 0.98);
        assertEquals(900, pacotesNecessarios, "Devem ser necessários 900 sucessos seguidos para recuperar a meta.");
    }

    @Test
    @DisplayName("STRESS: Deve retornar o total de falhas se a meta inserida for de 100% ou mais")
    void deveTratarMetaImpossivelNaProjecao() {
        List<Entrega> entregas = new ArrayList<>();
        Entrega entregaStress = new Entrega(1L, null, LocalDate.now(), 80, 20);
        entregas.add(entregaStress);

        int pacotesNecessarios = entregaService.calcularProjecaoPlatina(entregas, 1.0);
        assertEquals(20, pacotesNecessarios, "Se a meta for maior ou igual a 100%, o sistema deve retornar o total de falhas.");
    }

    @Test
    @DisplayName("MOCK: Deve buscar as entregas do mês atual simulando o comportamento do banco de dados")
    void deveObterEntregasDoMesAtualUsandoMock(){
        // Prepara uma entrega com a data de hoje (garantindo que passe no filter do Service)
        entregas.add(new Entrega(usuarioIdExemplo, null, LocalDate.now(), 50, 0));
        
        // CORREÇÃO: Configura o mock para responder ao método real chamado pelo Service (findByUsuarioId)
        when(repository.findByUsuarioId(usuarioIdExemplo))
                .thenReturn(entregas);

        // Executa a ação
        List<Entrega> resultado = entregaService.obterEntregasDoMesAtual(usuarioIdExemplo);

        // Asserções (Validações)
        assertNotNull(resultado, "O resultado retornado pelo serviço não pode ser nulo.");
        assertEquals(1, resultado.size(), "A lista deve conter exatamente 1 entrega simulada pelo mock.");
        assertEquals(50, entregaService.getTotalPacotes(resultado), "O total de pacotes deve somar 50 conforme configurado.");

        // CORREÇÃO: Verifica se o método correto foi invocado no repositório
        verify(repository, times(1)).findByUsuarioId(usuarioIdExemplo);
    }
}