package dreamjob.controller;

import dreamjob.dto.FileDto;
import dreamjob.model.User;
import dreamjob.model.Vacancy;
import dreamjob.service.SimpleCityService;
import dreamjob.service.SimpleFileService;
import dreamjob.service.SimpleVacancyService;
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
@RequestMapping("/vacancies")
public class VacancyController {

    private final SimpleVacancyService vacancyService;
    private  final SimpleCityService cityService;
    private final SimpleFileService fileService;

    public VacancyController(SimpleVacancyService vacancyService, SimpleCityService cityService, SimpleFileService fileService) {
        this.vacancyService = vacancyService;
        this.cityService = cityService;
        this.fileService = fileService;
    }

    @GetMapping
    public String getAll(Model model, HttpSession session) {
        model.addAttribute("vacancies", vacancyService.findAll());
        User user = Utility.logUser(model, session);
        model.addAttribute("user", user);
        return "vacancies/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model, HttpSession session) {
        model.addAttribute("cities", cityService.findAll());
        User user = Utility.logUser(model, session);
        model.addAttribute("user", user);
        return "vacancies/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Vacancy vacancy, @RequestParam MultipartFile file, Model model) {
        try {
            vacancyService.save(vacancy, new FileDto(file.getOriginalFilename(), file.getBytes()));
            return "redirect:/vacancies";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        Optional vacancyOptional = vacancyService.findById(id);
        if (vacancyOptional.isEmpty()) {
            model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("vacancy", vacancyOptional.get());
        return "vacancies/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Vacancy vacancy, @RequestParam MultipartFile file, Model model) {
        try {
            var isUpdated = vacancyService.update(vacancy, new FileDto(file.getOriginalFilename(), file.getBytes()));
            if (!isUpdated) {
                model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
                return "errors/404";
            }
            return "redirect:/vacancies";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        Boolean isDeleted = vacancyService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/vacancies";
    }
}

