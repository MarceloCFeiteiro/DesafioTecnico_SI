package tests.simulacoes;

import helpers.GerarDadosSimulacaoHelper;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tests.BaseTest;

@Tag("Regrecao")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteSimulacoesTest extends BaseTest {

    @Test
    public void DeletarSimulacaoSucesso() {
        JSONObject payload = GerarDadosSimulacaoHelper.gerarSimulacaoPadrao(cpfSimulacao);

        Response respostaPost = postSimulacaoRequest.PostSimulacao(payload);

        String idSimulacao = respostaPost.jsonPath().getString("id");

        deleteSimulacaoRequest.deleteSimulacao(idSimulacao)
                .then()
                .statusCode(204);
    }

    //Não é retornada mensagem de simulação inexistente.
    //Dever ser implementada a validação da mensagem, quando a api retornar a mesma
    @Test
    public void DeletarSimulacaoInexistente() {

        deleteSimulacaoRequest.deleteSimulacao("1111111111111111111")
                .then()
                .statusCode(404);
    }
}
