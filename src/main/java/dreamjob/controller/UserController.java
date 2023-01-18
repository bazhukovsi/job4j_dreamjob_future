package dreamjob.controller;

import dreamjob.model.User;
import dreamjob.service.SimpleUserService;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
@RequestMapping("/users")
public class UserController {

    private final SimpleUserService userService;

    public UserController(SimpleUserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getRegistrationPage() {
        return "users/registration";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/vacancies";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
    var userOptional = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
    if (userOptional.isEmpty()) {
        model.addAttribute("error", "Почта или пароль введены неверно");
        return "users/login";
    }
    var session = request.getSession();
    session.setAttribute("user", userOptional.get());
    return "redirect:/vacancies";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }
}
