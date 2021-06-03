package com.cargo.service;

import com.cargo.exception.CargoTranspNotFoundException;
import com.cargo.model.transportation.*;
import com.cargo.repos.TariffRepo;
import com.cargo.repos.TransportationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransportationService {

    @Autowired
    TransportationRepo transportationRepo;
    @Autowired
    TariffRepo tariffRepo;

    public Page<Transportation> findTransportation(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            return transportationRepo.findAll(pageable);
    }

    public Page<Transportation> findTransportationDest(int pageNo, int pageSize, String sortBy, String sortDir, String destination) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Optional<Page<Transportation>> page = Optional.ofNullable(transportationRepo.findTransportationByTariffAddress(Address.valueOf(destination), pageable));

        return page.orElse(Page.empty());

    }

    public Page<Transportation> findTransportationByDate(int pageNo, int pageSize, String sortBy, String sortDir, LocalDate date){

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Optional<Page<Transportation>> page = Optional.ofNullable(transportationRepo.findTransportationByCreationDate(date, pageable));
        return page.orElse(Page.empty());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void saveTransportation(Transportation tr, String address, String size, String weight) {

        tr.setTariff(tariffRepo.findTariffByAddressEqualsAndSizeEqualsAndWeightEquals(
                Address.valueOf(address), Size.valueOf(size), Weight.valueOf(weight)));
        tr.setDeliveryDate(tr.getCreationDate().plusDays(tariffRepo.
                findFirstByAddressEquals(Address.valueOf(address)).getDeliveryTermDays()));
        tr.setTransportationStatus(TransportationStatus.NEW);

        transportationRepo.save(tr);
    }


//    public Iterable<Transportation> findAll() {
//        return transportationRepo.findAll();
//    }


    public List<Transportation> getUserTransportations(Long id){
        return transportationRepo.findTransportationByCustomerId(id);
    }

    public Transportation findById(Long id){
        return transportationRepo.findById(id)
                .orElseThrow(() -> new CargoTranspNotFoundException(id.toString())); //TODO
    }


    public void transportationUpdate(Transportation tr, String status){
        tr.setTransportationStatus(TransportationStatus.valueOf(status));
        transportationRepo.save(tr);
    }
        //пэйджинация
    public Page<Transportation> findPaginated(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo -1, pageSize);
        return transportationRepo.findAll(pageable);
    }

    public void transportationPaymentComplete(Transportation tr){
        tr.setTransportationStatus(TransportationStatus.PAID);
        transportationRepo.save(tr);
    }
}
