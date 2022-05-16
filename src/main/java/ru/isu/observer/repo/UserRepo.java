package ru.isu.observer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.isu.observer.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

}
