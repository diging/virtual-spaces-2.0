package edu.asu.diging.vspace.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import edu.asu.diging.simpleusers.core.config.SimpleUsers;
import edu.asu.diging.simpleusers.core.config.SimpleUsersConfiguration;

@Configuration
@PropertySource("classpath:/config.properties")
public class SimpleUserConfig implements SimpleUsersConfiguration {

    @Value("${email_user}")
    private String emailUser;

    @Value("${email_password}")
    private String emailPassword;

    @Value("${email_host}")
    private String emailHost;

    @Value("${email_port}")
    private String emailPort;

    @Value("${email_from}")
    private String emailFrom;

    @Value("${app_url}")
    private String appUrl;

    @Override
    public void configure(SimpleUsers simpleUsers) {
        simpleUsers.usersEndpointPrefix("/staff/user/").userListView("staff/user/list").emailUsername(emailUser)
                .emailPassword(emailPassword).emailServerHost(emailHost).emailServerPort(emailPort).emailFrom(emailFrom)
                .instanceUrl(appUrl);
    }

}
