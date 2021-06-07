package com.cargo.controller;

import com.cargo.model.transportation.*;
import com.cargo.model.user.User;
import com.cargo.service.TransportationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@Controller
public class TranspController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TranspController.class);

    @Autowired
    private TransportationService transportationService;

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
        LOGGER.info("User " + currentUser.getUsername() + " has visited his orders page");
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
        LOGGER.info("User " + currentUser.getUsername() + " has visited order status edit page, order id: " + id);
        return "transportationPay";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/paymentComplete")
    public String paymentComplete(
            @AuthenticationPrincipal User user,
            @RequestParam("transportationId") Transportation transportation
    ){
        transportationService.transportationPaymentComplete(transportation);
        LOGGER.info("User " + user.getUsername() + " has changed order status, order id: " + transportation.getId());
        return "redirect:/user-transp/" + user.getUsername();
    }


    @PostMapping("/saveTransportation")
    public String saveNewTransportation(
            @RequestParam String address,
            @RequestParam String size,
            @RequestParam String weight,
            @Valid @ModelAttribute("newTransportation") Transportation transportation,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal User user
    ){
        if(bindingResult.hasErrors()){
            Map<String, String> errorsMap = getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
        } else {
            transportation.setCustomer(user);
            transportationService.saveTransportation(transportation, address, size, weight);
            LOGGER.warn("User " + user.getUsername() + " has created a new order");
        }
        return "redirect:/user-transp/" + user.getUsername();
    }

    static Map<String, String> getErrors(BindingResult bindingResult){
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage);
        Map<String, String> errorsMap = bindingResult.getFieldErrors().stream().collect(collector);
        return errorsMap;
    }

}
