package com.restoapi.controller;

import com.restoapi.dto.TableDto;
import com.restoapi.service.DiningTableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * Get all dining tables.
     * @return all tables in a list.
     */
    @GetMapping
    public List<TableDto> getTables() {
        return tableService.getTables();
    }

}