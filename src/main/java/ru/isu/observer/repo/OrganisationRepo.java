package ru.isu.observer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.observer.model.global.Organisation;

public interface OrganisationRepo extends JpaRepository<Organisation, Long> {
    Organisation getByName(String name);
}
