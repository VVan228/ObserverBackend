package ru.isu.observer.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.isu.observer.model.test.TestAnswer;

import java.util.List;

public interface TestAnswerRepo extends JpaRepository<TestAnswer, Long> {

    @Query("select ta from TestAnswer ta join ta.test t where t.id = :testId")
    List<TestAnswer> getTestAnswersByTestId(@Param("testId") Long testId);
    @Query("select ta from TestAnswer ta join ta.test t where t.id = :testId")
    Page<TestAnswer> getTestAnswersByTestId(Pageable pageable, @Param("testId") Long testId);

    @Query("select ta from TestAnswer ta join ta.student s where s.id=:id")
    List<TestAnswer> getStudentTestAnswers(@Param("id") Long userId);
    @Query("select ta from TestAnswer ta join ta.student s where s.id=:id")
    Page<TestAnswer> getStudentTestAnswers(Pageable pageable, @Param("id") Long userId);

    @Query("select ta from TestAnswer ta join ta.student s where s.id=:id and ta.validated=true")
    List<TestAnswer> getStudentTestAnswersValidated(@Param("id") Long userId);
    @Query("select ta from TestAnswer ta where ta.student.id=:id and ta.validated=true")
    Page<TestAnswer> getStudentTestAnswersValidated(Pageable pageable, @Param("id") Long userId);

    @Modifying(clearAutomatically = true)
    @Query("update TestAnswer ta set ta.totalScore = :score where ta.id = :id")
    void updateTotalScore(@Param("id") Long id, @Param("score")int score);

}
