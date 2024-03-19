package me.jysh.cinematic.controller;

import me.jysh.cinematic.model.Movie;
import me.jysh.cinematic.model.BookingRequest;
import me.jysh.cinematic.repository.MovieRepository;
import me.jysh.cinematic.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "http://localhost:3000")
public class MovieController {

    private final MovieRepository movieRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public MovieController(MovieRepository movieRepository, BookingRepository bookingRepository) {
        this.movieRepository = movieRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        for (Movie movie : movies) {
            movie.setFilledCapacity(movie.calculateFilledCapacity());
        }
        return ResponseEntity.ok(movies);
    }

    @PostMapping("/{id}/book")
    public ResponseEntity<String> bookMovieTickets(@PathVariable Long id, @RequestBody BookingRequest bookingRequest) {
        // Find the movie by id
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie == null) {
            return ResponseEntity.notFound().build();
        }

        // Check available seats
        int totalSeats = movie.getTotalSeats();
        int filledSeats = bookingRepository.countByMovieId(id);
        int requestedSeats = bookingRequest.getNumTickets();
        if (filledSeats + requestedSeats > totalSeats) {
            return ResponseEntity.badRequest().body("Not enough seats available for booking.");
        }

        // Save booking
        BookingRequest booking = new BookingRequest();
        booking.setMovie(movie);
        booking.setName(bookingRequest.getName());
        booking.setEmail(bookingRequest.getEmail());
        booking.setNumTickets(bookingRequest.getNumTickets());
        bookingRepository.save(booking);

        // Update filled capacity
        double filledCapacityPercentage = ((double) (filledSeats + requestedSeats) / totalSeats) * 100;
        movie.setFilledCapacity((int) filledCapacityPercentage);
        movieRepository.save(movie);

        return ResponseEntity.ok("Booking successful!");
    }

}
