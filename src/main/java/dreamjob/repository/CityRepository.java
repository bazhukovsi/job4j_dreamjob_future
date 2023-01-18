package dreamjob.repository;

import dreamjob.model.City;
import java.util.Collection;

public interface CityRepository {
    Collection<City> findAll();

    City fingById(int id);
}
