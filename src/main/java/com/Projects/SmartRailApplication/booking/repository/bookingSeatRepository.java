package com.Projects.SmartRailApplication.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Projects.SmartRailApplication.booking.Entities.BookingSeat;

public interface bookingSeatRepository extends JpaRepository<BookingSeat, Long>{

    
}
