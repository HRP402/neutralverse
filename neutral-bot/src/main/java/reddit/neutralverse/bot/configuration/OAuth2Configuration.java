package reddit.neutralverse.bot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.token.RequestEnhancer;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Configuration
public class OAuth2Configuration {

    @Bean
    public RestTemplate redditRestTemplate(ResourceOwnerPasswordResourceDetails redditResourceDetails,
                                           ResourceOwnerPasswordAccessTokenProvider redditAccessTokenProvider) {
        var template = new OAuth2RestTemplate(redditResourceDetails);
        template.setAccessTokenProvider(redditAccessTokenProvider);
        template.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add(HttpHeaders.USER_AGENT, redditResourceDetails.getId());
            return execution.execute(request, body);
        });
        return template;
    }

    @Bean
    public ResourceOwnerPasswordResourceDetails redditResourceDetails(ClientRegistrationRepository repository,
                                                                      @Value("${spring.security.oauth2.client.registration.reddit.username}") String username,
                                                                      @Value("${spring.security.oauth2.client.registration.reddit.password}") String password) {
        ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
        ClientRegistration registration = repository.findByRegistrationId("reddit");
        details.setClientId(registration.getClientId());
        details.setClientSecret(registration.getClientSecret());
        details.setAccessTokenUri(registration.getProviderDetails().getTokenUri());
        details.setGrantType(registration.getAuthorizationGrantType().getValue());
        details.setUsername(username);
        details.setPassword(password);
        details.setScope(new ArrayList(registration.getScopes()));
        details.setId(registration.getClientName());
        return details;
    }

    @Bean
    public ResourceOwnerPasswordAccessTokenProvider redditAccessTokenProvider(RequestEnhancer userAgentRequestEnhancer) {
        var provider = new ResourceOwnerPasswordAccessTokenProvider();
        provider.setTokenRequestEnhancer(userAgentRequestEnhancer);
        return provider;
    }

    @Bean
    public RequestEnhancer userAgentRequestEnhancer(@Value("${spring.security.oauth2.client.registration.reddit.client-name}") String clientName) {
        return (request, resource, form, headers) -> headers.add(HttpHeaders.USER_AGENT, clientName);
    }

}
