package aggregator;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {

    @RequestMapping("/aggregate")
    public ArrayList<Transaction> aggregate(@RequestParam @NonNull String account) {
        RestTemplate restTemplate = new RestTemplate();

        String url1 = "http://localhost:8888/transactions?account=" + account;
        String url2 = "http://localhost:8889/transactions?account=" + account;

        ResponseEntity<List<Transaction>> response1 =
                restTemplate.exchange(url1, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        ResponseEntity<List<Transaction>> response2 =
                restTemplate.exchange(url2, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        var result = new ArrayList<Transaction>();
        result.addAll(response1.getBody());
        result.addAll(response2.getBody());
        result.sort((t1, t2) -> -1 * t1.timestamp.compareTo(t2.timestamp));
        return result;
    }

    public record Transaction(String id, String serverId, String account, String amount, String timestamp) {}
}
