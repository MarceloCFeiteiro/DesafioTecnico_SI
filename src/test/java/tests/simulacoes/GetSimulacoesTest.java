package tests.simulacoes;

import io.restassured.response.Response;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("Regrecao")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetSimulacoesTest extends BaseTest {

    @Test
    public void validarBuscaTodasSimulacoes() {

        Response resposta = getSimulacaoRequest.getTodaSimulacoes();

        assertEquals(200, resposta.statusCode());

        int quantidadeSimulacoes = resposta.jsonPath().getList("$").size();
        for (int i = 0; i < quantidadeSimulacoes; i++) {
            assertNotNull(resposta.jsonPath().getString("[" + i + "].id"));
            assertNotNull(resposta.jsonPath().getString("[" + i + "].nome"));
            assertNotNull(resposta.jsonPath().getString("[" + i + "].cpf"));
            assertNotNull(resposta.jsonPath().getString("[" + i + "].email"));
            assertNotNull(resposta.jsonPath().getString("[" + i + "].valor"));
            assertNotNull(resposta.jsonPath().getString("[" + i + "].parcelas"));
            assertNotNull(resposta.jsonPath().getString("[" + i + "].seguro"));
        }
    }

    @Test
    public void validarBuscaSimulacoesPorCpf() {

        Response resposta = getSimulacaoRequest.getSimulacao(cpfJaCadastrado);

        assertEquals(200, resposta.statusCode());
        assertEquals("Teste Marcelo", resposta.jsonPath().getString("nome"));
        assertEquals(cpfJaCadastrado, resposta.jsonPath().getString("cpf"));
        assertEquals("marcelo.teste@google.com", resposta.jsonPath().getString("email"));
        assertEquals(1253.11, Double.parseDouble(resposta.jsonPath().getString("valor")));
        assertEquals(2, Integer.parseInt(resposta.jsonPath().getString("parcelas")));
        assertEquals(true, Boolean.parseBoolean(resposta.jsonPath().getString("seguro")));
    }

    @Test
    public void validarBuscaSimulacaoInexistente() {

        Response resposta = getSimulacaoRequest.getSimulacao("72358169021");

        assertEquals(404, resposta.statusCode());
        assertEquals("CPF 72358169021 não encontrado", resposta.jsonPath().getString("mensagem"));
    }

}
