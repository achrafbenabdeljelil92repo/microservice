package org.achraf.ws.authservice.listener;

import org.achraf.ws.authservice.entities.Activity;
import org.achraf.ws.authservice.entities.User;
import org.achraf.ws.authservice.enums.ActivityType;
import org.achraf.ws.authservice.repositories.ActivityAuthRepository;
import org.achraf.ws.authservice.repositories.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginListener {

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {

       /* String username = event.getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        Activity activity = new Activity();
        activity.setActivityType(ActivityType.LOGIN_FAILED);
        activity.setUser(user);
        activityAuthRepository.save(activity);*/
    }
    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {

    }
}
