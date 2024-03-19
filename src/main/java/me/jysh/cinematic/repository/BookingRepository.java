package me.jysh.cinematic.repository;

import me.jysh.cinematic.model.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingRequest, Long> {
    int countByMovieId(Long movieId);
}
