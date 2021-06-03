package com.cargo.service;

import com.cargo.model.transportation.Address;
import com.cargo.model.transportation.Size;
import com.cargo.model.transportation.Tariff;
import com.cargo.model.transportation.Weight;
import com.cargo.model.user.Role;
import com.cargo.repos.TariffRepo;
import com.sun.el.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class TariffService {
    private static final Logger logger = LoggerFactory.getLogger(TariffService.class);

    @Autowired
    private TariffRepo tariffRepo;

    public List<Tariff> findAll(){
        logger.debug("Loading all tariffs with findAll() method");
        return tariffRepo.findAll();
    }

    public Page<Tariff> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection){
        logger.debug("Loading all tariffs paginated with findPaginated() method");
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo -1, pageSize, sort);
        return tariffRepo.findAll(pageable);
    }

//    public Tariff findTariff(String address, String size, String weight){
//
//        return tariffRepo.findTariffByAddressEqualsAndSizeEqualsAndWeightEquals(
//                Address.valueOf(address), Size.valueOf(size), Weight.valueOf(weight));
//
//    }
//
//    public Long findDeliveryTerm(String address){
//        Tariff t = tariffRepo.findFirstByAddressEquals(Address.valueOf(address));
//        return t.getDeliveryTermDays();
//    }
}
