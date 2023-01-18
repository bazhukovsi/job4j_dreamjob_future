package dreamjob.service;

import dreamjob.model.City;
import dreamjob.repository.Sql2oCityRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SimpleCityService implements CityService {

    Sql2oCityRepository sql2oCityRepository;

    public SimpleCityService(Sql2oCityRepository sql2oCityRepository) {
        this.sql2oCityRepository = sql2oCityRepository;
    }

    @Override
    public Collection<City> findAll() {
        return sql2oCityRepository.findAll();
    }

    @Override
    public City findById(int id) {
        return sql2oCityRepository.fingById(id);
    }
}
