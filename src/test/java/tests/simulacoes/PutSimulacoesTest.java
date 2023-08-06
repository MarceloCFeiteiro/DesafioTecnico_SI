package tests.simulacoes;

import helpers.GerarDadosSimulacaoHelper;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import requests.simulacoes.PutSimulacoesRequest;
import tests.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Regrecao")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PutSimulacoesTest extends BaseTest {

    PutSimulacoesRequest putSimulacoesRequest = new PutSimulacoesRequest();

    @BeforeEach
    public void beforeEachPut() {
        JSONObject payload = GerarDadosSimulacaoHelper.gerarSimulacaoPadrao(cpfSimulacao);
        postSimulacaoRequest.PostSimulacao(payload);
    }

    @Test
    public void validarAlteracaoSimulacaoSucesso() {
        JSONObject payload = GerarDadosSimulacaoHelper.gerarSimulacaoParaAlteracao(cpfSimulacao);

        Response resposta = putSimulacoesRequest.putSimulacao(payload, cpfSimulacao);

        assertEquals(200, resposta.statusCode());
        assertEquals("Teste Marcelo Alterado", resposta.jsonPath().getString("nome"));
        assertEquals(cpfSimulacao, resposta.jsonPath().getString("cpf"));
        assertEquals("marceloAlterado.teste@google.com", resposta.jsonPath().getString("email"));
        assertEquals(1999.99, Double.parseDouble(resposta.jsonPath().getString("valor")));
        assertEquals(40, Integer.parseInt(resposta.jsonPath().getString("parcelas")));
        assertEquals(false, Boolean.parseBoolean(resposta.jsonPath().getString("seguro")));
    }

    @Test
    public void validarAlteracaoSimulacaoCpfNaoEncontrado() {
        JSONObject payload = GerarDadosSimulacaoHelper.gerarSimulacaoParaAlteracao(cpfSimulacao);

        Response resposta = putSimulacoesRequest.putSimulacao(payload, "75479707299");

        assertEquals(404, resposta.statusCode());
        assertEquals("CPF 75479707299 não encontrado", resposta.jsonPath().getString("mensagem"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"@teste.com", "testeteste.com", "teste@teste"})
    public void validarAlteracaoSimulacaoCampoEmailInconsistente(String email) {
        JSONObject payload = GerarDadosSimulacaoHelper.gerarSimulacaoParaAlteracao(cpfSimulacao);
        payload.put("email", email);

        Response resposta = putSimulacoesRequest.putSimulacao(payload, cpfSimulacao);

        assertEquals(400, resposta.statusCode());
        assertEquals("E-mail deve ser um e-mail válido", resposta.jsonPath().getString("erros.email"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, 0, 999.99, 40000.01})
    public void validarAlteracaoSimulacaoCampoValorInconsistente(double valor) {
        JSONObject payload = GerarDadosSimulacaoHelper.gerarSimulacaoParaAlteracao(cpfSimulacao);
        payload.put("valor", valor);

        Response resposta = putSimulacoesRequest.putSimulacao(payload, cpfSimulacao);

        assertEquals(400, resposta.statusCode());
        assertEquals("Valor deve ser menor ou igual a R$ 40.000", resposta.jsonPath().getString("erros.valor"));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1, 49})
    public void validarAlteracaoSimulacaoCampoParcelaInconsistente(int parcela) {
        JSONObject payload = GerarDadosSimulacaoHelper.gerarSimulacaoParaAlteracao(cpfSimulacao);
        payload.put("parcelas", parcela);

        Response resposta = putSimulacoesRequest.putSimulacao(payload, cpfSimulacao);

        assertEquals(400, resposta.statusCode());
        assertEquals("Parcelas deve ser igual ou maior que 2", resposta.jsonPath().getString("erros.parcelas"));
    }

}
