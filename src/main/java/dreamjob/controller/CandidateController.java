package dreamjob.controller;

import dreamjob.dto.FileDto;
import dreamjob.model.Candidate;
import dreamjob.model.User;
import dreamjob.service.SimpleCandidateService;
import dreamjob.service.SimpleCityService;
import dreamjob.service.SimpleFileService;
import dreamjob.utility.Utility;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@ThreadSafe
@RequestMapping("/candidates")
public class CandidateController {
    private final SimpleCandidateService candidateService;
    private final SimpleCityService cityService;
    private final SimpleFileService fileService;

    public CandidateController(SimpleCandidateService candidateService, SimpleCityService cityService, SimpleFileService fileService) {
        this.candidateService = candidateService;
        this.cityService = cityService;
        this.fileService = fileService;
    }

    @GetMapping
    public String getAll(Model model, HttpSession session) {
        model.addAttribute("candidates", candidateService.findAll());
        User user = Utility.logUser(model, session);
        model.addAttribute("user", user);
        return "candidates/candidates";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model, HttpSession session) {
        model.addAttribute("cities", cityService.findAll());
        User user = Utility.logUser(model, session);
        model.addAttribute("user", user);
        return "candidates/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Candidate candidate, @RequestParam MultipartFile file, Model model) {
        try {
            candidateService.save(candidate, new FileDto(file.getOriginalFilename(), file.getBytes()));
            return "redirect:/candidates";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        Optional candidateOptional = candidateService.findById(id);
        if (candidateOptional.isEmpty()) {
            model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("candidate", candidateOptional.get());
        return "candidates/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Candidate candidate, @RequestParam MultipartFile file, Model model) {
        try {
            var isUpdated = candidateService.update(candidate, new FileDto(file.getOriginalFilename(), file.getBytes()));
            if (!isUpdated) {
                model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
                return "errors/404";
            }
            return "redirect:/candidates";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        Boolean isDeleted = candidateService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/candidates";
    }

    @GetMapping("/test")
    public String testBootstrap() {
        return "candidates/test";
    }
}
