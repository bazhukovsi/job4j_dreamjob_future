package dreamjob.service;

import dreamjob.dto.FileDto;
import dreamjob.model.Vacancy;
import dreamjob.repository.Sql2oVacancyRepository;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@ThreadSafe
public class SimpleVacancyService implements VacancyService {

    private final Sql2oVacancyRepository sql2oVacancyRepository;
    private final FileService fileService;

    public SimpleVacancyService(Sql2oVacancyRepository sql2oVacancyRepository, FileService fileService) {
        this.sql2oVacancyRepository = sql2oVacancyRepository;
        this.fileService = fileService;
    }

    @Override
    public Vacancy save(Vacancy vacancy, FileDto image) {
        saveNewFile(vacancy, image);
        return sql2oVacancyRepository.save(vacancy);
    }

    private void saveNewFile(Vacancy vacancy, FileDto image) {
        var file = fileService.save(image);
        vacancy.setFileId(file.getId());
    }

    @Override
    public boolean deleteById(int id) {
        var fileOptional = findById(id);
        if (fileOptional.isEmpty()) {
            return false;
        }
        var isDeleted = sql2oVacancyRepository.deleteById(id);
        fileService.deleteById(fileOptional.get().getFileId());
        return isDeleted;
    }

    @Override
    public boolean update(Vacancy vacancy, FileDto image) {
        var isNewFileEmpty = image.getContent().length == 0;
        if (isNewFileEmpty) {
            return sql2oVacancyRepository.update(vacancy);
        }
        var oldFileId = vacancy.getFileId();
        saveNewFile(vacancy, image);
        var isUpdated = sql2oVacancyRepository.update(vacancy);
        fileService.deleteById(oldFileId);
        return isUpdated;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return sql2oVacancyRepository.findById(id);
    }

    @Override
    public Collection<Vacancy> findAll() {
        return sql2oVacancyRepository.findAll();
    }
}
