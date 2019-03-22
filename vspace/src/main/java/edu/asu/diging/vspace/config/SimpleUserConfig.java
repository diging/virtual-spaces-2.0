package edu.asu.diging.vspace.config;

import org.springframework.context.annotation.Configuration;

import edu.asu.diging.simpleusers.core.config.SimpleUsers;
import edu.asu.diging.simpleusers.core.config.SimpleUsersConfiguration;

@Configuration
public class SimpleUserConfig implements SimpleUsersConfiguration {

    @Override
    public void configure(SimpleUsers simpleUsers) {
        simpleUsers.usersEndpointPrefix("/staff/user/").userListView("staff/user/list");
    }

}
