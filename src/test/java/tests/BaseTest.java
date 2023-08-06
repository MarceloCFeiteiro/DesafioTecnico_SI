package tests;

import helpers.GerarDadosSimulacaoHelper;
import io.restassured.RestAssured;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import requests.simulacoes.DeleteSimulacaoRequest;
import requests.simulacoes.GetSimulacaoRequest;
import requests.simulacoes.PostSimulacaoRequest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    protected static final String cpfSimulacao = "47574228140";
    protected static final String cpfJaCadastrado = "82022786432";

    protected PostSimulacaoRequest postSimulacaoRequest = new PostSimulacaoRequest();
    protected DeleteSimulacaoRequest deleteSimulacaoRequest = new DeleteSimulacaoRequest();
    protected GetSimulacaoRequest getSimulacaoRequest = new GetSimulacaoRequest();

    @BeforeAll
    public void beforeAll() {
        RestAssured.baseURI = "http://localhost:8080/";

        JSONObject payload = GerarDadosSimulacaoHelper.gerarSimulacaoPadrao(cpfJaCadastrado);

        postSimulacaoRequest.PostSimulacao(payload);
    }

    @BeforeEach
    public void beforeEach() {
        deletarRegistros(cpfSimulacao);
    }

    @AfterAll
    public void afterAll() {
        deletarRegistros(cpfJaCadastrado);
    }

    private void deletarRegistros(String cpf) {
        String id = "";

        try {
            id = getSimulacaoRequest.getSimulacao(cpf).then().extract().path("id").toString();
        } catch (NullPointerException npe) {
            //Colocado para garantir o funcionamento do código, pois não temos o controle do BD para garantirmos
            //a existência de dados consistentes.
        }
        
        deleteSimulacaoRequest.deleteSimulacao(id);
    }
}