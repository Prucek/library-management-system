package cz.muni.fi.pa165.seminar3;

import java.time.ZoneId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring MVC Controller.
 * Handles OAuth2 login.
 */
@RestController
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    /**
     * Display access token.
     */
    @GetMapping("/")
    public OAuth2AccessToken index(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oauth2Client) {
        log.debug("***************************************************");
        log.debug("* index() called                                  *");
        log.debug("***************************************************");

        OAuth2AccessToken accessToken = oauth2Client.getAccessToken();
        // log access token
        log.debug("access token principal: {}", oauth2Client.getPrincipalName());
        log.debug("access token scopes: {}", accessToken.getScopes());
        log.debug("access token issued: {}", accessToken.getIssuedAt().atZone(ZoneId.systemDefault()));
        log.debug("access token expires: {}", accessToken.getExpiresAt().atZone(ZoneId.systemDefault()));
        log.debug("access token value: {}", accessToken.getTokenValue());

        return accessToken;
    }
}
