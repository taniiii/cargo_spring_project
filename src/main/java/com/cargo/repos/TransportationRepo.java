package com.cargo.repos;

import com.cargo.model.transportation.Address;
import com.cargo.model.transportation.Transportation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface TransportationRepo extends JpaRepository<Transportation, Long> {

    Page<Transportation> findTransportationByCommentContaining(String filter, Pageable pageable);

    Page<Transportation> findTransportationByTariffAddress(Address a, Pageable pageable);

    Page<Transportation> findTransportationByCreationDate(LocalDate filter, Pageable pageable);

    List<Transportation> findTransportationByCustomerId(Long id);

}
