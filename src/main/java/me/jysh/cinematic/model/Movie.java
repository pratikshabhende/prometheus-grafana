package me.jysh.cinematic.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Date;


@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String genre;
    private String language;
    private double weekdayTicketPrice;
    private double weekendTicketPrice;
    private int capacity;
    private double filledCapacity;

    // Annotate showtime field with @JsonInclude to handle null gracefully
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String showtime;

    @Column(name = "total_seats")
    private int totalSeats;

    private BigDecimal pricePerTicket;

    @OneToMany(mappedBy = "movie")
    private List<BookingRequest> bookings;

    private String date;


    public int calculateFilledCapacity() {
        return (int) ((bookings.size() / (double) totalSeats) * 100);
    }

    // Method to calculate the total seats based on capacity
    public int getTotalSeats() {
        return totalSeats;
    }

    // Constructor

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getWeekdayTicketPrice() {
        return weekdayTicketPrice;
    }

    public void setWeekdayTicketPrice(double weekdayTicketPrice) {
        this.weekdayTicketPrice = weekdayTicketPrice;
    }

    public double getWeekendTicketPrice() {
        return weekendTicketPrice;
    }

    public void setWeekendTicketPrice(double weekendTicketPrice) {
        this.weekendTicketPrice = weekendTicketPrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getFilledCapacity() {
        return filledCapacity;
    }

    public void setFilledCapacity(double filledCapacity) {
        this.filledCapacity = filledCapacity;
    }

    public String getShowtime() {
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getPricePerTicket() {
        return pricePerTicket;
    }

    public void setPricePerTicket(BigDecimal pricePerTicket) {
        this.pricePerTicket = pricePerTicket;
    }

    public boolean isWeekend() {
        if (showtime == null) {
            return false;
        }
        try {
            // Parse the showtime string and extract the day of the week
            LocalDate date = LocalDate.parse(showtime);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            // Check if the day of the week is Saturday or Sunday
            return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
        } catch (DateTimeParseException e) {
            // If there's an error parsing the date, return false
            return false;
        }
    }
}
