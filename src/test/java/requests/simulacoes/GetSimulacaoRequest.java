package requests.simulacoes;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetSimulacaoRequest {
    public Response getSimulacao(String cpf) {
        return given()
                .when()
                .get("api/v1/simulacoes/" + cpf);
    }

    public Response getTodaSimulacoes() {
        return given()
                .when()
                .get("api/v1/simulacoes/");
    }
}
