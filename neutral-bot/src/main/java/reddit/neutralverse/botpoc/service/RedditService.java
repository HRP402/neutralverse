package reddit.neutralverse.botpoc.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RedditService {

    private RestTemplate restTemplate;

    @Autowired
    public RedditService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /*public String getModActions(String subreddit) {

    }*/

    public void test() {
        JsonNode node = restTemplate.getForObject(
                "https://oauth.reddit.com/api/v1/me", JsonNode.class);

        System.err.println(node);



/*        HttpHeaders headers = new HttpHeaders();
        //headers.set("User-agent", "hrp402_test");

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                "https://oauth.reddit.com/api/v1/me", HttpMethod.GET, entity, JsonNode.class);
        System.err.println(response);*/
        return;
    }

}
