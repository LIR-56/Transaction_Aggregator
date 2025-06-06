package aggregator;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {

    @RequestMapping("/aggregate")
    public ArrayList<Transaction> aggregate(@RequestParam @NonNull String account) {

        String url1 = "http://localhost:8888/transactions?account=" + account;
        String url2 = "http://localhost:8889/transactions?account=" + account;


        var result = new ArrayList<Transaction>();
        result.addAll(requestAndRetryFiveTimes(url1));
        result.addAll(requestAndRetryFiveTimes(url2));
        result.sort((t1, t2) -> -1 * t1.timestamp.compareTo(t2.timestamp));
        return result;
    }

    private List<Transaction> requestAndRetryFiveTimes(String url) {
        RestTemplate restTemplate = new RestTemplate();
        int i = 0;
        while (i < 5) {
            ResponseEntity<List<Transaction>> response;
            try {
                response =
                        restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
                if (response.getStatusCode() == HttpStatusCode.valueOf(200)){
                    return response.getBody();
                }
            } catch (RestClientException ignored) {
                i++;
            }
        }
        return List.of();
    }

    public record Transaction(String id, String serverId, String account, String amount, String timestamp) {
    }
}
