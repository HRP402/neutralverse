package reddit.neutralverse.botpoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.RequestEnhancer;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.client.RestTemplate;
import reddit.neutralverse.botpoc.service.RedditService;

import java.util.ArrayList;

@SpringBootApplication
//@EnableOAuth2Client
public class BotPocApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BotPocApplication.class, args);
    }

    /*
        This bean is not needed. It is an example of converting a ClientRegistration to OAuth2ProtectedResourceDetails for
        use in OAuth2RestTemplate. This way will allow multiple registrations.
     */
    //@Bean
    public OAuth2ProtectedResourceDetails redditResourceDetails(ClientRegistrationRepository repository) {
        BaseOAuth2ProtectedResourceDetails details = new ClientCredentialsResourceDetails();
        ClientRegistration registration = repository.findByRegistrationId("reddit");
        details.setClientId(registration.getClientId());
        details.setClientSecret(registration.getClientSecret());
        details.setAccessTokenUri(registration.getProviderDetails().getTokenUri());
        details.setGrantType(registration.getAuthorizationGrantType().getValue());
        details.setScope(new ArrayList(registration.getScopes()));
        details.setId(registration.getClientName());
        return details;
    }

    @Bean
    public RestTemplate redditRestTemplate(OAuth2ProtectedResourceDetails details,
                                           RequestEnhancer userAgentRequestEnhancer,
                                           ClientHttpRequestInterceptor userAgentRequestInterceptor) {
        OAuth2RestTemplate template = new OAuth2RestTemplate(details);
        ClientCredentialsAccessTokenProvider provider = new ClientCredentialsAccessTokenProvider();
        provider.setTokenRequestEnhancer(userAgentRequestEnhancer);
        template.setAccessTokenProvider(provider);
        template.getInterceptors().add(userAgentRequestInterceptor);
        return template;
    }

    @Bean
    public RequestEnhancer userAgentRequestEnhancer() {
        return (request, resource, form, headers) -> headers.add("User-agent", "hrp402_test");
    }

    @Bean
    public ClientHttpRequestInterceptor userAgentRequestInterceptor() {
        return (request, body, execution) -> {
            request.getHeaders().add("User-agent", "hrp402_test");
            return execution.execute(request, body);
        };
    }

    @Autowired
    private RedditService redditService;

    @Override
    public void run(String... args) throws Exception {
        redditService.test();
    }
}
