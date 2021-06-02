package com.cargo.service;

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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.cargo.service.ServiceUtils.getParameters;

@Service
public class TransportationService {

    @Autowired
    TransportationRepo transportationRepo;
    @Autowired
    TariffRepo tariffRepo;

//    public List<Transportation> findTransportation(String filter) {
//        if(filter != null && !filter.isEmpty()){
//            return transportationRepo.findTransportationByCommentContaining(filter);
//        } else {
//            return transportationRepo.findAll();
//        }
//    }
    public Page<Transportation> findTransportation(int pageNo, int pageSize, String sortBy, String sortDir, String filter) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort); //-1
        if(filter != null && !filter.isEmpty()){
            return transportationRepo.findTransportationByCommentContaining(filter, pageable);
        } else {
            return transportationRepo.findAll(pageable);
        }
    }

    public Page<Transportation> findTransportationDest(int pageNo, int pageSize, String sortBy, String sortDir, String destination) {
        Set<String> addresses = getParameters(Address.values()); //TODO убрать повторяющийся код
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        for(String e : addresses){
            if(e.contains(destination.toUpperCase())){
                return transportationRepo.findTransportationByTariffAddress(Address.valueOf(e), pageable);
            }
        }

        return Page.empty();
    }

    public Page<Transportation> findTransportationByDate(int pageNo, int pageSize, String sortBy, String sortDir, String filter){

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        if(filter.matches("\\d{4}-\\d{2}-\\d{2}")){
            return transportationRepo.findTransportationByCreationDate(LocalDate.parse(filter), pageable);
        } else
            return Page.empty();

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


    public Iterable<Transportation> findAll() {
        return transportationRepo.findAll();
    }


    public List<Transportation> getUserTransportations(Long id){
        return transportationRepo.findTransportationByCustomerId(id);
    }

    public Transportation findById(Long id){
        return transportationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Transportation not found for id :: " + id)); //TODO
    }

//    public void transportationUpdate(Transportation tr, Map<String, String> transpStatus){ //String commen
////        tr.setComment(comment);
//
////        Set<String> roles = Arrays.stream(Role.values()) //смотрим какие роли есть вообще
////                .map(Role::name)
////                .collect(Collectors.toSet());
////
////        user.getRoles().clear(); //очищаем все раннее присутствовавшие роли пользователя
////
////        for(String key : form.keySet()){ //проверяем, что форма содержит роли для пользователя
////            if(roles.contains(key)){     //кроме ролей в списке есть токены и ИД, кот. не нужны
////                user.getRoles().add(Role.valueOf(key));
////            }
////        }
//        Set<String> allStatuses = getParameters(TransportationStatus.values());
//        for (String key : transpStatus.keySet()){
//            if(allStatuses.contains(key)){
//                tr.setTransportationStatus(TransportationStatus.valueOf(key));
//            }
//        }
//        transportationRepo.save(tr);
//    }

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
