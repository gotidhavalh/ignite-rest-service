package io.project.app.resources;

import io.project.app.model.Flight;
import io.project.app.repositories.FlightRepository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class FlightController {

    private static final Logger log = LoggerFactory.getLogger(FlightController.class);

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping(path = "/flights", produces = "application/json;charset=UTF-8")
    public List<Flight> findAll() {
        log.debug("REST request to get all Blogs");
        Iterable<Flight> flightList = flightRepository.findAll();
        List<Flight> flights = new ArrayList<Flight>();
        for(Flight flight : flightList) {
        	flights.add(flight);
        }
        return flights;
    }

    @GetMapping(path = "/flight/{id}", produces = "application/json;charset=UTF-8")
    public Flight findOne(@PathVariable Long id) {
        log.debug("REST request to get one");
        System.out.println("id is  " + id);
        Flight model = flightRepository.findOne(id);       
        //Flight model = flightRepository.findById(id).orElse(null);
        return model;
    }

    @PostMapping(
        path = "/flight/{id}",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<Flight> addOne(@RequestBody Flight flight, @PathVariable Long id, UriComponentsBuilder ucb) {
        log.debug("REST request to insert one");
        Flight savedFlight = flightRepository.save(id, flight);

        return ResponseEntity
            .created(ucb.pathSegment("/api/flight/{id}").buildAndExpand(savedFlight.getId()).toUri())
            .body(savedFlight);
    }
    
}
