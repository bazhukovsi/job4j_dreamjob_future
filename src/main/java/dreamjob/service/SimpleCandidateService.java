package dreamjob.service;

import dreamjob.dto.FileDto;
import dreamjob.model.Candidate;
import dreamjob.repository.Sql2oCandidateRepository;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@ThreadSafe
public class SimpleCandidateService implements CandidateService {

    private final Sql2oCandidateRepository sql2oCandidateRepository;
    private final FileService fileService;

    public SimpleCandidateService(Sql2oCandidateRepository sql2oCandidateRepository, FileService fileService) {
        this.sql2oCandidateRepository = sql2oCandidateRepository;
        this.fileService = fileService;
    }

    @Override
    public Candidate save(Candidate candidate, FileDto image) {
        saveNewFile(candidate, image);
        return sql2oCandidateRepository.save(candidate);
    }

    private void saveNewFile(Candidate candidate, FileDto image) {
        var file = fileService.save(image);
        candidate.setFileId(file.getId());
    }

    @Override
    public boolean deleteById(int id) {
        return sql2oCandidateRepository.deleteById(id);
    }

    @Override
    public boolean update(Candidate candidate, FileDto image) {
        var isNewFileEmpty = image.getContent().length == 0;
        if (isNewFileEmpty) {
            return sql2oCandidateRepository.update(candidate);
        }
        var oldFileId = candidate.getFileId();
        saveNewFile(candidate, image);
        var isUpdated = sql2oCandidateRepository.update(candidate);
        fileService.deleteById(oldFileId);
        return isUpdated;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return sql2oCandidateRepository.findById(id);
    }

    @Override
    public Collection<Candidate> findAll() {
        return sql2oCandidateRepository.findAll();
    }
}
