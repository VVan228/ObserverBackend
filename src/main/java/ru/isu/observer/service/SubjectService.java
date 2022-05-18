package ru.isu.observer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
    UserRepo userRepo;

    @Value("${pages.size}")
    private Integer PAGE_SIZE;

    @Autowired
    public SubjectService(SubjectRepo subjectRepo, UserRepo userRepo) {
        this.subjectRepo = subjectRepo;
        this.userRepo = userRepo;
    }

    public void addSubject(Subject s){
        subjectRepo.save(s);
    }


    public List<Subject> getSubjects(Long organisationId){
        return subjectRepo.getSubjectsOfOrganisation(organisationId);
    }
    public Page<Subject> getSubjectsPage(Long organisationId, Sort.Direction direction, Integer page, String sortBy){
        return subjectRepo.getSubjectsOfOrganisation(
                PageRequest.of(
                        page,
                        PAGE_SIZE,
                        direction,
                        sortBy
                ),
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
    @Transactional
    public Subject setOrganisation(Subject subject, Long organisationId){
        subject.setOrganisationId(organisationId);
        subjectRepo.save(subject);
        return subject;
    }

    @Transactional
    public void updateSubjectName(Long id, String name){
        subjectRepo.updateName(id, name);
    }
    @Transactional
    public Subject updateSubjectName(Subject subject, String name){
        if(subject.getId() == null){
            subjectRepo.save(subject);
        }
        Long id = subject.getId();
        subjectRepo.updateName(id, name);
        return getSubject(id);
    }

    @Transactional
    public Subject addTeacherToSubject(Long subjectId, Long teacherId){
        User teacher = userRepo.getById(teacherId);
        Subject subject = subjectRepo.getById(subjectId);
        subject.addTeacher(teacher);
        subjectRepo.save(subject);
        return subject;
    }
    @Transactional
    public Subject addTeacherToSubject(Subject subject, Long teacherId){
        User teacher = userRepo.getById(teacherId);
        subject.addTeacher(teacher);
        subjectRepo.save(subject);
        return subject;
    }
    @Transactional
    public Subject addTeacherToSubject(Subject subject, User teacher){
        subject.addTeacher(teacher);
        subjectRepo.save(subject);
        return subject;
    }
    @Transactional
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
