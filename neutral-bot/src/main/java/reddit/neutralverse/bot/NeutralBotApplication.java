package reddit.neutralverse.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reddit.neutralverse.bot.service.RedditService;

@SpringBootApplication
public class NeutralBotApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NeutralBotApplication.class, args);
    }

    @Autowired
    private RedditService redditService;

    @Override
    public void run(String... args) throws Exception {
        redditService.test();
        redditService.getModLog();  // Needs a user
    }
}
