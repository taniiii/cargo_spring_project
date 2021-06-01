package com.cargo.controller;

import com.cargo.model.transportation.Tariff;
import com.cargo.model.transportation.Transportation;
import com.cargo.model.user.User;
import com.cargo.service.TariffService;
import com.cargo.service.TransportationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class TranspController {

    @Autowired
    private TransportationService transportationService;
    @Autowired
    private TariffService tariffService;

    @GetMapping("/user-transp/{name}")
    public String getUserTransportations(
            @PathVariable(value="name") String userName,
            @AuthenticationPrincipal User currentUser,
            Model model
    ){
        if(currentUser.getUsername().equals(userName)){
            model.addAttribute("transportations", transportationService
                    .getUserTransportations(currentUser.getId()));
        }
        return "userTransportations";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/transp/{id}")
    public String transportationEditForm(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(value = "id") Long id, Model model){
        Transportation tr = transportationService.findById(id);
        if(tr.getCustomer().getId().equals(currentUser.getId())) {
            model.addAttribute("transportation", tr);
        }
        return "transportationPay";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/paymentComplete")
    public String paymentComplete(
            @AuthenticationPrincipal User user,
            @RequestParam("transportationId") Transportation transportation
    ){
        transportationService.transportationPaymentComplete(transportation);
        return "redirect:/user-transp/" + user.getUsername();
    }


    @PostMapping("/saveTransportation")
    @Transactional
    public String saveNewTransportation(
//            @RequestParam Map<String, String> tariffParam,
            @RequestParam String address,
            @RequestParam String size,
            @RequestParam String weight,
            @ModelAttribute("newTransportation") Transportation transportation,
            @AuthenticationPrincipal User user
    ){
        transportation.setCustomer(user);
        //transportation.setTariff(tariffService.findTariff(tariffParam));
        transportation.setTariff(tariffService.findTariff(address, size, weight));
        transportation.setDeliveryDate(transportation.getCreationDate().plusDays(tariffService.findDeliveryTerm(address)));
        transportationService.saveTransportation(transportation);

        return "redirect:/user-transp/" + user.getUsername();
    }
}
