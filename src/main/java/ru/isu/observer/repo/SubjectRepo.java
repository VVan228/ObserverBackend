package ru.isu.observer.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.isu.observer.model.global.Subject;
import ru.isu.observer.model.global.SubjectPlain;
import ru.isu.observer.model.user.Role;
import ru.isu.observer.model.user.User;

import javax.persistence.Id;
import java.util.List;

public interface SubjectRepo extends JpaRepository<Subject, Long> {

    @Query("select new ru.isu.observer.model.global.SubjectPlain(s.id, s.name) from Subject s where s.organisationId = :organisationId")
    List<SubjectPlain> getSubjectsOfOrganisation(@Param("organisationId") Long organisationId);

    @Query("select s from Subject s where s.organisationId = :organisationId")
    Page<Subject> getSubjectsOfOrganisation(Pageable pageable, @Param("organisationId") Long organisationId);

    @Modifying(clearAutomatically = true)
    @Query("update Subject s set s.organisationId = :organisationId where s.id = :id")
    void setOrganisation(@Param(value = "id") Long id, @Param(value = "organisationId") Long organisationId);

    @Modifying(clearAutomatically = true)
    @Query(value = "update Subject s set s.name = :name where s.id = :id")
    void updateName(@Param(value = "id") Long id, @Param(value = "name") String name);

    Subject findByName(String name);
}
