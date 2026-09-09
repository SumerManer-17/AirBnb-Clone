package com.example.airBnbApp.repository;

import com.example.airBnbApp.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
