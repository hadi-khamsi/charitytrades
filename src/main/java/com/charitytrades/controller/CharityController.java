package com.charitytrades.controller;

import com.charitytrades.dto.CharityDTO;
import com.charitytrades.repository.CharityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charities")
public class CharityController {

    private final CharityRepository charityRepository;

    public CharityController(CharityRepository charityRepository) {
        this.charityRepository = charityRepository;
    }

    @GetMapping
    public List<CharityDTO> getAllCharities() {
        return charityRepository.findAll().stream()
                .map(CharityDTO::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharityDTO> getCharityById(@PathVariable Long id) {
        return charityRepository.findById(id)
                .map(charity -> ResponseEntity.ok(CharityDTO.fromEntity(charity)))
                .orElse(ResponseEntity.notFound().build());
    }
}
