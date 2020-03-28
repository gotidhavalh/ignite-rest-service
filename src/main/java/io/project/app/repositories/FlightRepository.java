package io.project.app.repositories;


import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

import io.project.app.model.Flight;

@Repository
@RepositoryConfig(cacheName = "FlightCache")
public interface FlightRepository extends IgniteRepository<Flight, Long> {    
    
//    @Override
//    List<Flight> findAll();
//    
//    @Override
//    Flight findOne(Long id);
}
