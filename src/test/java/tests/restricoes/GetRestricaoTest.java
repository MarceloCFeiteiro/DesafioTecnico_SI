package tests.restricoes;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import requests.restricoes.GetRestricaoRequest;
import tests.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Regrecao")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetRestricaoTest extends BaseTest {
    GetRestricaoRequest getRestricaoRequest = new GetRestricaoRequest();

    //Teste quebrando devido a diferença nas mensagens da doc e da api
    @Test
    public void validarCpfComRestricao() {
        String cpfRestrito = "97093236014";

        String mensagem = getRestricaoRequest.getRestricao(cpfRestrito)
                .then()
                .statusCode(200)
                .extract()
                .path("mensagem");

        assertEquals(mensagem, "O CPF " + cpfRestrito + " possui restrição");
    }

    @Test
    public void validarCpfSemRestricao() {
        String cpfRestrito = "26111243454";

        getRestricaoRequest.getRestricao(cpfRestrito)
                .then()
                .statusCode(204);
    }

    //A api não valida valores inconsistentes de entrada e nem seu tamanho máximo, retornando 204 para qualquer caso
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"8112204535", "811220453526", "8112t045352", "11111111111"})
    public void validarCpfsComInconsistencias(String parametro) {
        getRestricaoRequest.getRestricao(parametro)
                .then()
                .statusCode(204);
    }
}
