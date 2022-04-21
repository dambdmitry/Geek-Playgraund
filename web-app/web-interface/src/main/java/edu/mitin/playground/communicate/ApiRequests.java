package edu.mitin.playground.communicate;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiRequests {

    private final static HttpClient CLIENT = HttpClient.newHttpClient();

    private String host = "http://localhost";
    private String port = "9000";
    private String apiContext = "api/v1/";

    public enum ActionRequestBody {
        VERIFY("verification/verify-solution");


        private String endpoint;

        ActionRequestBody(String endpoint) {
            this.endpoint = endpoint;
        }
    }

    public enum ActionRequestParams {
        START_TOURNAMENT("tournament/start-tournament", List.of("tournamentId"));

        private String endpoint;
        private List<String> params;

        ActionRequestParams(String endpoint, List<String> params) {
            this.endpoint = endpoint;
            this.params = params;
        }
    }

    public String sendPost(ActionRequestBody actionRequestBody, String body) throws IOException, InterruptedException {
        HttpResponse<String> re = CLIENT.send(HttpRequest.newBuilder()
                .uri(URI.create(buildUrl()+ actionRequestBody.endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(body)).header("Content-Type", "application/json")
                .build(), HttpResponse.BodyHandlers.ofString());
        return re.body();
    }

    public String sendPostRequestParams(ActionRequestParams action, List<String> paramsValue) throws IOException, InterruptedException {
        final String str = buildUrl() + action.endpoint + buildRequestParams(action.params, paramsValue);
        HttpResponse<String> re = CLIENT.send(HttpRequest.newBuilder()
                .uri(URI.create(str))
                .POST(HttpRequest.BodyPublishers.noBody()).header("Content-Type", "application/json")
                .build(), HttpResponse.BodyHandlers.ofString());
        return re.body();
    }

    private String buildRequestParams(List<String> params, List<String> paramsValue) {
        if (params.size() != paramsValue.size()) return "";
        StringBuilder result = new StringBuilder("?");
        for (int i = 0; i < params.size(); i++) {
            result.append(params.get(i)).append("=").append(paramsValue.get(i));
            if (i != params.size() - 1) {
                result.append("&");
            }
        }
        return result.toString();
    }

    private String buildUrl(){
        return host + ":" + port + "/" + apiContext;
    }
}
