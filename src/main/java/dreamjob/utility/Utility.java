package dreamjob.utility;

import dreamjob.model.User;
import org.springframework.ui.Model;
import javax.servlet.http.HttpSession;

public class Utility {
    public static User logUser(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        return user;
    }
}
