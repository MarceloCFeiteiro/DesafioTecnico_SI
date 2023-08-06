package requests.simulacoes;

import io.restassured.response.Response;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.given;

public class PutSimulacoesRequest {

    public Response putSimulacao(JSONObject body, String cpf) {
        return given()
                .header("Content-Type", "application/json")
                .when()
                .body(body.toString())
                .put("api/v1/simulacoes/" + cpf);
    }
}
