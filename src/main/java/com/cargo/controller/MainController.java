package com.cargo.controller;

import com.cargo.model.transportation.*;
import com.cargo.service.TariffService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private TariffService tariffService;

    @GetMapping ("/")
    public String greeting(Map<String, Object> model){
        return "home";
    }

    @GetMapping("/tariffs")
    public String getTariffs(Model model){
        return getTariffsPaginated(1, "address", "asc", model);
    }

    @GetMapping ("/tariffs/{pageNo}")
    public String getTariffsPaginated(@PathVariable(value = "pageNo") int pageNo,
                             @RequestParam("sortField") String sortField,
                             @RequestParam("sortDirection") String sortDirection,
                             Model model){
        int pageSize = 10;
        Page<Tariff> page = tariffService.findPaginated(pageNo, pageSize, sortField, sortDirection);

        List<Tariff> tariffList = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");

        model.addAttribute("tariffList", tariffList);

        return "tariffs";
    }

    @GetMapping("/showNewTransportationForm")
    public String showNewTransportationForm(Model model){
        Transportation tr = new Transportation();
        model.addAttribute("newTransportation", tr);
        model.addAttribute("addresses", Address.values());
        model.addAttribute("weights", Weight.values());
        model.addAttribute("sizes", Size.values());
        return "newTransportation";
    }
}
