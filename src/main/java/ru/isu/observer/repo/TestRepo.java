package ru.isu.observer.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.isu.observer.model.test.Test;

import java.util.List;

public interface TestRepo extends JpaRepository<Test, Long> {

    @Query("select t from Test t join t.openedFor u where u=:id")
    List<Test> getTestsForStudent(@Param("id") Long id);


    @Query("select t from Test t where t.creator.id=:id")
    List<Test> getTestsForTeacher(@Param("id") Long id);

    @Query("select t from Test t where t.creator.id=:id and t.autoCheck=false")
    List<Test> getNotAutoCheckTests(@Param("id") Long teacherId);


    @Query("select t from Test t join t.openedFor u where u=:id")
    Page<Test> getTestsForStudentPage(Pageable pageable, @Param("id") Long id);


    @Query("select t from Test t where t.creator.id=:id")
    Page<Test> getTestsForTeacherPage(Pageable pageable, @Param("id") Long id);

    @Query("select t from Test t where t.creator.id=:id and t.autoCheck=false")
    Page<Test> getNotAutoCheckTestsPage(Pageable pageable, @Param("id") Long teacherId);
}
