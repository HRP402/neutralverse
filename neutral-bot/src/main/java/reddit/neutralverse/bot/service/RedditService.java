package reddit.neutralverse.bot.service;

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

    public void test() {
        JsonNode node = restTemplate.getForObject(
                "https://oauth.reddit.com/api/v1/me", JsonNode.class);

        System.err.println(node);
    }

    public void getModLog() {
        JsonNode modLog = restTemplate.getForObject("https://oauth.reddit.com/r/HR_Paperstacks_402/about/log", JsonNode.class);
        System.err.println(modLog);
    }

}
