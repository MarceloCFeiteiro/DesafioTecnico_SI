package helpers;

import org.json.simple.JSONObject;

public class GerarDadosSimulacaoHelper {

    public static JSONObject gerarSimulacaoPadrao(String cpfSimulacao) {

        JSONObject payLoad = new JSONObject();
        payLoad.put("nome", "Teste Marcelo");
        payLoad.put("cpf", cpfSimulacao);
        payLoad.put("email", "marcelo.teste@google.com");
        payLoad.put("valor", 1253.11);
        payLoad.put("parcelas", 2);
        payLoad.put("seguro", true);

        return payLoad;
    }
    public static JSONObject gerarSimulacaoParaAlteracao(String cpfSimulacao) {

        JSONObject payLoad = new JSONObject();
        payLoad.put("nome", "Teste Marcelo Alterado");
        payLoad.put("email", "marceloAlterado.teste@google.com");
        payLoad.put("valor", 1999.99);
        payLoad.put("parcelas", 40);
        payLoad.put("seguro", false);

        return payLoad;
    }
}
