package com.restoapi.controller;

import com.restoapi.dto.TableDto;
import com.restoapi.service.DiningTableService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
     *
     * @return all tables as dtos in a list.
     */
    @GetMapping("/tables")
    public List<TableDto> getTables(
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) LocalDateTime end,
            @RequestParam(required = false) Integer guests
    ) {
        return tableService.getTables(start, end, guests);
    }

}