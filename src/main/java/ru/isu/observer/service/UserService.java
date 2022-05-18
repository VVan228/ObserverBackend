package ru.isu.observer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.isu.observer.model.user.Role;
import ru.isu.observer.model.user.User;
import ru.isu.observer.repo.UserRepo;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    @Value("${pages.size}")
    private Integer PAGE_SIZE;

    UserRepo userRepo;

    @Autowired
    UserService(
            UserRepo userRepo
    ){
        this.userRepo = userRepo;
    }

    public void saveStudent(User user){
        user.setRole(Role.STUDENT);
        userRepo.save(user);
    }
    public void saveUser(User user){
        userRepo.save(user);
    }
    public void saveTeacher(User user){
        user.setRole(Role.TEACHER);
        userRepo.save(user);
    }

    @Transactional
    public void setOrganisation(Long id, Long organisationId){
        userRepo.setOrganisation(id, organisationId);
    }
    @Transactional
    public User setOrganisation(User user, Long organisationId){
        user.setOrganisationId(organisationId);
        userRepo.save(user);
        return user;
    }


    public User getUser(Long id){
        return userRepo.getById(id);
    }
    public User findUserByEmail(String email){
        return userRepo.getByEmail(email);
    }

    @Transactional
    public void updateUserName(Long id, String name){
        userRepo.updateName(id, name);
    }
    @Transactional
    public User updateUserName(User user, String name){
        user.setName(name);
        userRepo.save(user);
        return user;
    }

    @Transactional
    public void updateUserEmail(Long id, String email){
        userRepo.updateEmail(id, email);
    }
    @Transactional
    public User updateUserEmail(User user, String email){
        user.setEmail(email);
        userRepo.save(user);
        return user;
    }

    @Transactional
    public void replaceRefreshToken(Long id, String refreshToken){
        userRepo.replaceRefreshToken(id, refreshToken);
    }
    @Transactional
    public User replaceRefreshToken(User user, String refreshToken){
        user.setCurrentRefreshTokenHash(refreshToken);
        userRepo.save(user);
        return user;
    }

    public Page<User> getStudentsPage(Long organisationId, Sort.Direction direction, Integer page, String sortBy){
        return userRepo.getUsersOfOrganisation(
                PageRequest.of(
                        page,
                        PAGE_SIZE,
                        direction,
                        sortBy
                ),
                organisationId,
                Role.STUDENT);
    }
    public Page<User> getTeachersPage(Long organisationId, Sort.Direction direction, Integer page, String sortBy){
        return userRepo.getUsersOfOrganisation(
                PageRequest.of(
                        page,
                        PAGE_SIZE,
                        direction,
                        sortBy
                ),
                organisationId,
                Role.TEACHER);
    }
    public List<User> getStudents(Long organisationId){
        return userRepo.getUsersOfOrganisation(organisationId, Role.STUDENT);
    }
    public List<User> getTeachers(Long organisationId){
        return userRepo.getUsersOfOrganisation(organisationId, Role.TEACHER);
    }


    public void deleteUser(Long id){
        //userRepo.deleteById(id);
    }
}
