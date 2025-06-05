package aggregator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class PingController {

    @RequestMapping("/aggregate")
    public String aggregate() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8889/ping";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return response.getBody();
    }
}
