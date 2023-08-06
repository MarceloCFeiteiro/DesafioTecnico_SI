package requests.simulacoes;

import io.restassured.response.Response;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.given;

public class PostSimulacaoRequest {
    public Response PostSimulacao(JSONObject body) {
        return given()
                .header("Content-Type", "application/json")
                .when()
                .body(body.toString())
                .post("api/v1/simulacoes");
    }
}