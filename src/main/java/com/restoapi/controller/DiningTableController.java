package com.restoapi.controller;

import com.restoapi.service.DiningTableService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tables")
public class DiningTableController {

    private final DiningTableService tableService;

    /**
     * Dining table controller constructor.
     *
     * @param tableService DiningTableService.
     */
    public DiningTableController(DiningTableService tableService) {
        this.tableService = tableService;
    }

}