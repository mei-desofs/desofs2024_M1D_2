package com.isep.desofs.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.isep.desofs.domain.Portfolio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PortfolioDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PortfolioDTO)) {
            return false;
        }

        PortfolioDTO portfolioDTO = (PortfolioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, portfolioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PortfolioDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
