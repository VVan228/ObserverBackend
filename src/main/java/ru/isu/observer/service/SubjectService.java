package ru.isu.observer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.isu.observer.model.global.Organisation;
import ru.isu.observer.model.global.Subject;
import ru.isu.observer.model.user.Role;
import ru.isu.observer.model.user.User;
import ru.isu.observer.repo.SubjectRepo;
import ru.isu.observer.repo.UserRepo;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class SubjectService {

    SubjectRepo subjectRepo;
    UserService userService;


    @Autowired
    public SubjectService(SubjectRepo subjectRepo, UserService userService) {
        this.subjectRepo = subjectRepo;
        this.userService = userService;
    }

    public void addSubject(Subject s){
        subjectRepo.save(s);
    }


    public List<Subject> getSubjects(Long organisationId){
        return subjectRepo.getSubjectsOfOrganisation(organisationId);
    }
    public List<Subject> getSubjects(Organisation organisation){
        return getSubjects(organisation.getId());
    }
    public Page<Subject> getSubjectsPage(Organisation organisation, Pageable pageable){
        return getSubjectsPage(organisation.getId(), pageable);
    }
    public Page<Subject> getSubjectsPage(Long organisationId, Pageable pageable){
        return subjectRepo.getSubjectsOfOrganisation(
                pageable,
                organisationId);
    }

    public Subject getSubject(Long id){
        return subjectRepo.getById(id);
    }
    public Subject getSubjectByName(String name){
        return subjectRepo.findByName(name);
    }

    @Transactional
    public void setOrganisation(Long id, Long organisationId){
        subjectRepo.setOrganisation(id, organisationId);
    }
    public void setOrganisation(Subject subject, Long organisationId){
        subject.setOrganisationId(organisationId);
        subjectRepo.save(subject);
    }

    @Transactional
    public void updateSubjectName(Long id, String name){
        subjectRepo.updateName(id, name);
    }
    public void updateSubjectName(Subject subject, String name){
        subject.setName(name);
        subjectRepo.save(subject);
    }


    public Subject addTeacherToSubject(Long subjectId, Long teacherId){
        User teacher = userService.getUser(teacherId);
        Subject subject = subjectRepo.getById(subjectId);
        subject.addTeacher(teacher);
        subjectRepo.save(subject);
        return subject;
    }
    public void addTeacherToSubject(Subject subject, Long teacherId){
        User teacher = userService.getUser(teacherId);
        subject.addTeacher(teacher);
        subjectRepo.save(subject);
    }
    public void addTeacherToSubject(Subject subject, User teacher){
        subject.addTeacher(teacher);
        subjectRepo.save(subject);
    }
    public Subject addTeacherToSubject(Long subjectId, User teacher){
        Subject subject = subjectRepo.getById(subjectId);
        subject.addTeacher(teacher);
        subjectRepo.save(subject);
        return subject;
    }


    public void deleteSubject(Long id){
        //subjectRepo.deleteById(id);
    }
}
