package requests.restricoes;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class GetRestricaoRequest {

    public Response getRestricao(String cpf) {
        return given()
                .when()
                .get("api/v1/restricoes/" + cpf);
    }
}