package com.cargo.repos;

import com.cargo.model.transportation.Address;
import com.cargo.model.transportation.Size;
import com.cargo.model.transportation.Tariff;
import com.cargo.model.transportation.Weight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface TariffRepo extends JpaRepository<Tariff, Long> {

    Tariff findTariffByAddressEqualsAndSizeEqualsAndWeightEquals(Address address, Size size, Weight weight);

    Tariff findFirstByAddressEquals(Address address);
}
