package pl.coderslab.charity.registration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import pl.coderslab.charity.model.User;

import java.util.Locale;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private Locale locale;
    private User user;

    public OnRegistrationCompleteEvent(User user, Locale locale) {
        super(user);

        this.user = user;
        this.locale = locale;
    }
}
