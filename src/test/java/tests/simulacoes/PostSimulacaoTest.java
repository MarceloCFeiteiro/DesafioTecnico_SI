package tests.simulacoes;

import helpers.GerarDadosSimulacaoHelper;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tests.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Regrecao")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostSimulacaoTest extends BaseTest {

    @Test
    public void validarCriacaoSimulacaoSucesso() {
       JSONObject payload = GerarDadosSimulacaoHelper.gerarSimulacaoPadrao(cpfSimulacao);

        Response resposta = postSimulacaoRequest.PostSimulacao(payload);

        assertEquals(201, resposta.statusCode());
        assertEquals("Teste Marcelo", resposta.jsonPath().getString("nome"));
        assertEquals(cpfSimulacao, resposta.jsonPath().getString("cpf"));
        assertEquals("marcelo.teste@google.com", resposta.jsonPath().getString("email"));
        assertEquals(1253.11, Double.parseDouble(resposta.jsonPath().getString("valor")));
        assertEquals(2, Integer.parseInt(resposta.jsonPath().getString("parcelas")));
        assertEquals(true, Boolean.parseBoolean(resposta.jsonPath().getString("seguro")));
    }

    @Test
    public void validarCriacaoSimulacaoCamposNulos() {
        JSONObject payload = new JSONObject();
        payload.put("seguro", true);

        Response resposta = postSimulacaoRequest.PostSimulacao(payload);

        Assertions.assertAll(
                () -> assertEquals(400, resposta.statusCode()),
                () -> assertEquals("Nome não pode ser vazio", resposta.jsonPath().getString("erros.nome")),
                () -> assertEquals("CPF não pode ser vazio", resposta.jsonPath().getString("erros.cpf")),
                () -> assertEquals("E-mail não deve ser vazio", resposta.jsonPath().getString("erros.email")),
                () -> assertEquals("Valor não pode ser vazio", resposta.jsonPath().getString("erros.valor")),
                () -> assertEquals("Parcelas não pode ser vazio", resposta.jsonPath().getString("erros.parcelas"))
        );
    }

    @Test
    public void validarCriacaoSimulacaoCPFJaCadastrado() {
        JSONObject payload = GerarDadosSimulacaoHelper.gerarSimulacaoPadrao(cpfJaCadastrado);

        Response resposta = postSimulacaoRequest.PostSimulacao(payload);

        assertEquals(409, resposta.statusCode());
        assertEquals("CPF já existente", resposta.jsonPath().getString("mensagem"));
    }

    //Quebrando devido a variação de retorno da mensagem quando o email é inválido
    @ParameterizedTest
    @ValueSource(strings = {"@teste.com", "testeteste.com", "teste@teste"})
    public void validarCriacaoSimulacaoCampoEmailInconsistente(String email) {
        JSONObject payload = GerarDadosSimulacaoHelper.gerarSimulacaoPadrao(cpfSimulacao);
        payload.put("email", email);

        Response resposta = postSimulacaoRequest.PostSimulacao(payload);

        assertEquals(400, resposta.statusCode());
        assertEquals("E-mail deve ser um e-mail válido", resposta.jsonPath().getString("erros.email"));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, 0, 999.99, 40000.01})
    public void validarCriacaoSimulacaoCampoValorInconsistente(double valor) {
        JSONObject payload = GerarDadosSimulacaoHelper.gerarSimulacaoPadrao(cpfSimulacao);
        payload.put("valor", valor);

        Response resposta = postSimulacaoRequest.PostSimulacao(payload);

        assertEquals(400, resposta.statusCode());
        assertEquals("Valor deve ser menor ou igual a R$ 40.000", resposta.jsonPath().getString("erros.valor"));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1, 49})
    public void validarCriacaoSimulacaoCampoParcelaInconsistente(int parcela) {
        JSONObject payload = GerarDadosSimulacaoHelper.gerarSimulacaoPadrao(cpfSimulacao);
        payload.put("parcelas", parcela);

        Response resposta = postSimulacaoRequest.PostSimulacao(payload);

        assertEquals(400, resposta.statusCode());
        assertEquals("Parcelas deve ser igual ou maior que 2", resposta.jsonPath().getString("erros.parcelas"));
    }
}
