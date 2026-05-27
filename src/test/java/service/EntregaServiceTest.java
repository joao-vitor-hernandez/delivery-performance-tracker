package service;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Entrega;
import repository.EntregaRepository;

@ExtendWith(MockitoExtension.class)
public class EntregaServiceTest {

    @Mock
    private EntregaRepository repository;

    @InjectMocks
    private EntregaService entregaService;

    private List<Entrega> entregas;
    private int usuarioIdExemplo;

    @BeforeEach
    void setUp() {
        usuarioIdExemplo = 1;
        entregas = new ArrayList<>();
    }

    @Test
    @DisplayName("Deve calcular a taxa de sucesso corretamente com uma lista de entregas")
    void deveCalcularTaxaSucessoComSucesso() {
        // 1. Cenário (Given)
        List<Entrega> entregas = new ArrayList<>();
        
        // Instanciando usando o seu construtor real: usuarioId, data, sucessos, falhas
        Entrega entrega1 = new Entrega(1, LocalDate.now(), 95, 5);
        entregas.add(entrega1);

        // 2. Ação (When)
        double taxaCalculada = entregaService.calcularTaxaSucesso(entregas);

        // 3. Validação (Then)
        assertEquals(95.0, taxaCalculada, 0.001, "A taxa de sucesso para 95 sucessos de 100 totais deve ser 95.0%");
    }

    @Test
    @DisplayName("Deve retornar zero se o total de pacotes na lista for zero")
    void deveTratarDivisaoPorZeroAoCalcularTaxa() {
        // Cenário de stress: Lista com uma entrega sem nenhum pacote movimentado (0 sucessos e 0 falhas)
        List<Entrega> entregas = new ArrayList<>();
        Entrega entregaVazia = new Entrega(1, LocalDate.now(), 0, 0);
        entregas.add(entregaVazia);

        double taxaCalculada = entregaService.calcularTaxaSucesso(entregas);

        assertEquals(0.0, taxaCalculada, "Se não há pacotes movimentados, a taxa deve ser 0 para evitar divisão por zero.");
    }

    @Test
    @DisplayName("STRESS: Deve calcular corretamente a projeção para meta de 98% (Nível Platina)")
    void deveCalcularProjecaoPlatinaComSucesso() {
        // Cenário: Motorista com 80 sucessos e 20 falhas (Total: 100 pacotes, taxa atual: 80%)
        List<Entrega> entregas = new ArrayList<>();
        Entrega entregaStress = new Entrega(1, LocalDate.now(), 80, 20);
        entregas.add(entregaStress);

        // Ação: Calculando projeção para meta de 98% (0.98)
        int pacotesNecessarios = entregaService.calcularProjecaoPlatina(entregas, 0.98);

        // Validação matemática da sua fórmula: (0.98 * 100 - 80) / (1 - 0.98) = 18 / 0.02 = 900
        assertEquals(900, pacotesNecessarios, "Devem ser necessários 900 sucessos seguidos para recuperar a meta.");
    }

    @Test
    @DisplayName("STRESS: Deve retornar o total de falhas se a meta inserida for de 100% ou mais")
    void deveTratarMetaImpossivelNaProjecao() {
        // Cenário: O motorista possui 20 falhas, tornando impossível atingir 100% de sucesso absoluto
        List<Entrega> entregas = new ArrayList<>();
        Entrega entregaStress = new Entrega(1, LocalDate.now(), 80, 20);
        entregas.add(entregaStress);

        // Ação: Testando o limite da regra passando 1.0 (100% de meta)
        int pacotesNecessarios = entregaService.calcularProjecaoPlatina(entregas, 1.0);

        // Validação: A proteção do seu código deve retornar o número de falhas (20)
        assertEquals(20, pacotesNecessarios, "Se a meta for maior ou igual a 100%, o sistema deve retornar o total de falhas.");
    }

    @Test
    @DisplayName("MOCK: Deve buscar as entregas do mês atual simulando o comportamento do banco de dados")
    void deveObterEntregasDoMesAtualUsandoMock(){
        entregas.add(new Entrega(usuarioIdExemplo, LocalDate.now(), 50, 0));

        when(repository.buscarPorUsuario(eq(usuarioIdExemplo), anyInt(), anyInt())).thenReturn(entregas);

        List<Entrega> resultado = entregaService.obterEntregasDoMesAtual(usuarioIdExemplo);

        assertNotNull(resultado, "O resultado retornado pelo serviço não pode ser nulo.");
        assertEquals(1, resultado.size(), "A lista deve conter exatamente 1 entrega simulada pelo mock.");
        assertEquals(50, entregaService.getTotalPacotes(resultado), "O total de pacotes deve somar 50 conforme configurado.");

        verify(repository, times(1)).buscarPorUsuario(eq(usuarioIdExemplo), anyInt(), anyInt());
    }
}