package ru.isu.observer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.isu.observer.model.global.Organisation;
import ru.isu.observer.model.user.Role;
import ru.isu.observer.model.user.User;
import ru.isu.observer.repo.UserRepo;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {


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
    public User setOrganisation(User user, Long organisationId){
        user.setOrganisationId(organisationId);
        userRepo.save(user);
        return user;
    }
    public User setOrganisation(User user, Organisation organisation){
        user.setOrganisationId(organisation.getId());
        userRepo.save(user);
        return user;
    }
    public void setOrganisation(Long id, Organisation organisation){
       setOrganisation(id, organisation.getId());
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
    public User updateUserName(User user, String name){
        user.setName(name);
        userRepo.save(user);
        return user;
    }

    @Transactional
    public void updateUserEmail(Long id, String email){
        userRepo.updateEmail(id, email);
    }
    public User updateUserEmail(User user, String email){
        user.setEmail(email);
        userRepo.save(user);
        return user;
    }

    @Transactional
    public void replaceRefreshToken(Long id, String refreshToken){
        userRepo.replaceRefreshToken(id, refreshToken);
    }
    public User replaceRefreshToken(User user, String refreshToken){
        user.setCurrentRefreshTokenHash(refreshToken);
        userRepo.save(user);
        return user;
    }

    public Page<User> getStudentsPage(Long organisationId, Pageable pageable){
        return userRepo.getUsersOfOrganisation(
                pageable,
                organisationId,
                Role.STUDENT);
    }
    public Page<User> getTeachersPage(Long organisationId, Pageable pageable){
        return userRepo.getUsersOfOrganisation(
                pageable,
                organisationId,
                Role.TEACHER);
    }
    public Page<User> getStudentsPage(Organisation organisation, Pageable pageable){
        return userRepo.getUsersOfOrganisation(
                pageable,
                organisation.getId(),
                Role.STUDENT);
    }
    public Page<User> getTeachersPage(Organisation organisation, Pageable pageable){
        return userRepo.getUsersOfOrganisation(
                pageable,
                organisation.getId(),
                Role.TEACHER);
    }

    public List<User> getStudents(Long organisationId){
        return userRepo.getUsersOfOrganisation(organisationId, Role.STUDENT);
    }
    public List<User> getTeachers(Long organisationId){
        return userRepo.getUsersOfOrganisation(organisationId, Role.TEACHER);
    }
    public List<User> getStudents(Organisation organisation){
        return getStudents(organisation.getId());
    }
    public List<User> getTeachers(Organisation organisation){
        return getTeachers(organisation.getId());
    }


    public void deleteUser(Long id){
        //userRepo.deleteById(id);
    }
}
