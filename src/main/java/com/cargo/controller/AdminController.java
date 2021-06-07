package com.cargo.controller;

import com.cargo.model.transportation.Address;
import com.cargo.model.transportation.Transportation;
import com.cargo.model.transportation.TransportationStatus;
import com.cargo.service.TransportationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@PreAuthorize("hasAuthority('ADMIN')")
@Controller
public class AdminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    TransportationService transportationService;


    @GetMapping("orders")
    public String showAllTransportations(
            @RequestParam(defaultValue = "") String destinationFilter,
            @RequestParam(defaultValue = "") String date,
            Model model,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir){

        LOGGER.info("Orders page is visited by administrator");

        Page<Transportation> page = transportationService.findTransportation(pageNo, pageSize, sortBy, sortDir);

        if(!destinationFilter.isEmpty()){
            page = transportationService.findTransportationDest(pageNo, pageSize, sortBy, sortDir, destinationFilter);
        }
         if(!date.isEmpty()){
            page = transportationService.findTransportationByDate(pageNo, pageSize, sortBy, sortDir, LocalDate.parse(date));
        }

        List<Transportation> listOfOrders = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("sortBy", sortBy);
        model.addAttribute("addresses", Address.values());
        model.addAttribute("listOfOrders", listOfOrders);
        model.addAttribute("destinationFilter", destinationFilter);
        model.addAttribute("date", date);

        return "orders";
    }


    @GetMapping("/transportationConfirm/{id}")
    public String transportationEditForm(
            @PathVariable(value = "id") Transportation tr, Model model){

            model.addAttribute("statusAvailable", TransportationStatus.values());
            model.addAttribute("transportation", tr);
        LOGGER.info("Order status change page is visited by administrator, order id: " + tr.getId());
        return "transportationEdit";
    }


    @PostMapping("/transportationConfirm/{id}")
    public String transportationConfirm(
            @PathVariable(value="id") Transportation tr,
            @RequestParam String status
    ){
        transportationService.transportationUpdate(tr, status);
        LOGGER.info("Order status is changed by administrator, order id: " + tr.getId());
        return "redirect:/orders";
    }
}
