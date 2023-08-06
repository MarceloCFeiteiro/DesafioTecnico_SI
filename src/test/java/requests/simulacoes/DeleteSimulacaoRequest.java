package requests.simulacoes;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteSimulacaoRequest {
    public Response deleteSimulacao(String id) {
        return given()
                .when()
                .delete("api/v1/simulacoes/" + id);
    }
}