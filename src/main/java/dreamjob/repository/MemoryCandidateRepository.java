package dreamjob.repository;

import dreamjob.model.Candidate;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryCandidateRepository implements CandidateRepository {

    private final AtomicInteger nextId = new AtomicInteger(1);
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    public MemoryCandidateRepository() {
        save(new Candidate(0, "Ivan", "Java",
                LocalDateTime.now(), 1, 0));
        save(new Candidate(0, "Ivan Ivanov", "Java, Hibernate, JS, React",
                LocalDateTime.now(), 2, 0));
        save(new Candidate(0, "Petr Petrov", "Java, Hibernate, JS, React",
                LocalDateTime.now(), 2, 0));
        save(new Candidate(0, "Vasya Vasilyev", "Java, Hibernate, JS, React",
                LocalDateTime.now(), 3, 0));
        save(new Candidate(0, "Sasha Ivanova", "Java, Hibernate, JS, React",
                LocalDateTime.now(), 2, 0));
        save(new Candidate(0, "Masha Zvereva", "Java, Hibernate, JS, React",
                LocalDateTime.now(), 1, 0));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.getAndIncrement());
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldVacancy) ->
                        new Candidate(oldVacancy.getId(), candidate.getName(), candidate.getDescription(),
                                candidate.getCreationDate(), candidate.getCityId(), candidate.getFileId())) != null;
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
