package com.charitytrades.dto;

import com.charitytrades.entity.Charity;

public class CharityDTO {
    private Long id;
    private String name;
    private String description;

    public CharityDTO() {}

    public static CharityDTO fromEntity(Charity charity) {
        CharityDTO dto = new CharityDTO();
        dto.id = charity.getId();
        dto.name = charity.getName();
        dto.description = charity.getDescription();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
