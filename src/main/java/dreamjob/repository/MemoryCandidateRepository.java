package dreamjob.repository;

import dreamjob.model.Candidate;
import dreamjob.service.CandidateService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryCandidateRepository implements CandidateService {

    private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private int nextId = 1;

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    public MemoryCandidateRepository() {
        save(new Candidate(0, "Sergey Bazhukov", "Java, Hibernate", LocalDateTime.of(2022, 11, 20, 10, 00)));
        save(new Candidate(0, "Ivan Ivanov", "Java, Hibernate, JS, React", LocalDateTime.of(2022, 11, 20, 10, 00)));
        save(new Candidate(0, "Petr Petrov", "Java, Hibernate, JS, React", LocalDateTime.of(2022, 11, 20, 10, 00)));
        save(new Candidate(0, "Vasya Vasilyev", "Java, Hibernate, JS, React", LocalDateTime.of(2022, 11, 20, 10, 00)));
        save(new Candidate(0, "Sasha Ivanova", "Java, Hibernate, JS, React", LocalDateTime.of(2022, 11, 20, 10, 00)));
        save(new Candidate(0, "Masha Zvereva", "Java, Hibernate, JS, React", LocalDateTime.of(2022, 11, 20, 10, 00)));
    }

    public static MemoryCandidateRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        String formattedDateTime = candidate.getCreationDate().format(formatter);
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldVacancy) ->
                new Candidate(oldVacancy.getId(), candidate.getName(), candidate.getDescription(),
                        candidate.getCreationDate())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
