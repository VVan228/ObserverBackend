package ru.isu.observer.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.isu.observer.model.user.Role;
import ru.isu.observer.model.user.User;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {

    User getByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.name = :name where u.id = :id")
    void updateName(@Param(value = "id") Long id, @Param(value = "name") String name);

    @Query("select u.currentRefreshTokenHash from User u where u.email=:email")
    String getRefreshTokenByEmail(@Param("email") String email);


    @Modifying(clearAutomatically = true)
    @Query(value = "update User u set u.email = :email where u.id = :id")
    void updateEmail(@Param(value = "id") Long id, @Param(value = "email") String email);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.currentRefreshTokenHash = :token where u.id = :id")
    void replaceRefreshToken(@Param(value = "id") Long id, @Param(value = "token") String token);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.organisationId = :organisationId where u.id = :id")
    void setOrganisation(@Param(value = "id") Long id, @Param(value = "organisationId") Long organisationId);

    @Query("select u from User u where u.organisationId = :organisationId and u.role = :role")
    List<User> getUsersOfOrganisation(@Param("organisationId") Long organisationId, @Param("role") Role role);

    @Query("select u from User u where u.organisationId = :organisationId and u.role = :role")
    Page<User> getUsersOfOrganisation(
            Pageable pageable,
            @Param("organisationId") Long organisationId,
            @Param("role") Role role
    );

}
