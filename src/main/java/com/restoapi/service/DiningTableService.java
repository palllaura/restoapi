package com.restoapi.service;

import com.restoapi.repository.DiningTableRepository;
import org.springframework.stereotype.Service;


@Service
public class DiningTableService {
    private final DiningTableRepository tableRepository;

    /**
     * Dining table service constructor.
     *
     * @param tableRepository DiningTableRepository.
     */
    public DiningTableService(DiningTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

}
