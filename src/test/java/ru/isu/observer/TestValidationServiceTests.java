package ru.isu.observer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.isu.observer.model.test.*;
import ru.isu.observer.service.TestValidationService;

import java.util.*;

@SpringBootTest
public class TestValidationServiceTests {

    @Autowired
    TestValidationService testValidationService;

    //@Test
    //void contextLoads() {
    //}

    @Test
    void utilSetTest(){
        List<Variant> vars = new ArrayList<>();

        for(int i = 0; i<10; i++){
            Variant v = new Variant();
            v.setId((long)i);
            v.setText("jajoj");
            vars.add(v);
        }

        Variant t = new Variant();
        t.setId((long)1);
        t.setText("jajoj");
        assert vars.contains(t);
    }

    @Test
    void openValidation(){
        ru.isu.observer.model.test.Test test = new ru.isu.observer.model.test.Test();

        Random rand = new Random();
        Question q1 = new Question();
        Question q2 = new Question();
        Question q3 = new Question();
        Question q4 = new Question();
        q1.setQuestionType(QuestionType.OpenQuestion);
        q2.setQuestionType(QuestionType.OpenQuestion);
        q3.setQuestionType(QuestionType.OpenQuestion);
        q4.setQuestionType(QuestionType.OpenQuestion);
        q1.setId(rand.nextLong());
        q2.setId(rand.nextLong());
        q3.setId(rand.nextLong());
        q4.setId(rand.nextLong());
        Answer a1 = new Answer();
        Answer a2 = new Answer();
        Answer a3 = new Answer();
        Answer a4 = new Answer();
        a1.setOpenAnswer("rightAnsw1");
        a2.setOpenAnswer("rightAnsw2");
        a3.setOpenAnswer("rightAnsw3");
        a4.setOpenAnswer("rightAnsw4");
        q1.setRightAnswer(a1);
        q2.setRightAnswer(a2);
        q3.setRightAnswer(a3);
        q4.setRightAnswer(a4);

        Answer sa1 = new Answer();
        Answer sa2 = new Answer();
        Answer sa3 = new Answer();
        Answer sa4 = new Answer();
        sa1.setOpenAnswer("answ1");
        sa2.setOpenAnswer("rightAnsw2");
        sa3.setOpenAnswer("answ");
        sa4.setOpenAnswer("rightAnsw4");

        Map<Long, Answer> studAnswer = new HashMap<>();

        studAnswer.put(q1.getId(), sa1);
        studAnswer.put(q2.getId(), sa2);
        studAnswer.put(q3.getId(), sa3);
        studAnswer.put(q4.getId(), sa4);

        test.setQuestions(List.of(q1,q2,q3,q4));

        TestAnswer ta = testValidationService.validateTest(test, studAnswer);

        assert ta.getTotalScore()==200;
    }

    @Test
    void oneVarValidation(){
        ru.isu.observer.model.test.Test test = new ru.isu.observer.model.test.Test();

        Question q1 = generateOneVarQuestion("qq1");
        Question q2 = generateOneVarQuestion("qq2");
        Question q3 = generateOneVarQuestion("qq3");
        Question q4 = generateOneVarQuestion("qq4");

        Map<Long, Answer> studAnswer = new HashMap<>();

        studAnswer.put(q4.getId(), q4.getRightAnswer());
        studAnswer.put(q2.getId(), q2.getRightAnswer());
        studAnswer.put(q3.getId(), getOneVarWrongAnswer(q3));
        studAnswer.put(q1.getId(), getOneVarWrongAnswer(q1));

        test.setQuestions(List.of(q1,q2,q3,q4));

        TestAnswer ta = testValidationService.validateTest(test, studAnswer);

        assert ta.getTotalScore()==200;
    }

    @Test
    void mulVarValidation(){
        ru.isu.observer.model.test.Test test = new ru.isu.observer.model.test.Test();

        Question q1 = generateMulVarQuestion("qq1", 3, 5);
        Question q2 = generateMulVarQuestion("qq2", 3, 5);
        Question q3 = generateMulVarQuestion("qq3", 3, 5);
        Question q4 = generateMulVarQuestion("qq4",3 ,5);

        Map<Long, Answer> studAnswer = new HashMap<>();

        studAnswer.put(q1.getId(), getMulVarWrongAnswer(q1, 2, 1));
        studAnswer.put(q2.getId(), getMulVarWrongAnswer(q2, 2, 0));
        studAnswer.put(q3.getId(), getMulVarWrongAnswer(q3, 1, 2));
        studAnswer.put(q4.getId(), getMulVarWrongAnswer(q4, 1, 1));

        test.setQuestions(List.of(q1,q2,q3,q4));

        TestAnswer ta = testValidationService.validateTest(test, studAnswer);

        assert ta.getTotalScore() == 99;
    }

    @Test
    void testAutoCheckTest(){
        ru.isu.observer.model.test.Test test = new ru.isu.observer.model.test.Test();

        Question q1 = generateMulVarQuestion("qq1", 3, 5);
        Question q2 = generateMulVarQuestion("qq2", 3, 5);
        Question q3 = generateMulVarQuestion("qq3", 3, 5);
        Question q4 = new Question();
        q4.setQuestionType(QuestionType.OpenQuestionCheck);

        Map<Long, Answer> studAnswer = new HashMap<>();

        studAnswer.put(q1.getId(), getMulVarWrongAnswer(q1, 2, 1));
        studAnswer.put(q2.getId(), getMulVarWrongAnswer(q2, 2, 0));
        studAnswer.put(q3.getId(), getMulVarWrongAnswer(q3, 1, 2));
        studAnswer.put(q4.getId(), new Answer());

        test.setQuestions(List.of(q1,q2,q3,q4));

        TestAnswer ta = testValidationService.validateTest(test, studAnswer);

        assert !ta.isValidated();
    }

    @Test
    void orderValidation(){
        ru.isu.observer.model.test.Test test = new ru.isu.observer.model.test.Test();

        Question q1 = generateOrderQuestion("qq1", 6);
        Question q2 = generateOrderQuestion("qq2", 6);
        Question q3 = generateOrderQuestion("qq3", 6);
        Question q4 = generateOrderQuestion("qq4",6);

        Map<Long, Answer> studAnswer = new HashMap<>();

        studAnswer.put(q1.getId(), getOrderWrongAnswer(q1, 4, 2));
        studAnswer.put(q2.getId(), getOrderWrongAnswer(q2, 6, 0));
        studAnswer.put(q3.getId(), getOrderWrongAnswer(q3, 2, 4));
        studAnswer.put(q4.getId(), getOrderWrongAnswer(q4, 0, 6));

        test.setQuestions(List.of(q1,q2,q3,q4));

        TestAnswer ta = testValidationService.validateTest(test, studAnswer);

        assert ta.getTotalScore() == 199;
    }

    Question generateOneVarQuestion(String name){
        Question res = new Question();

        List<Variant> variants = new ArrayList<>();

        Random rand = new Random();
        int rightIndex = rand.nextInt(5);

        for(int i = 0; i<5; i++){
            Variant v = new Variant();

            if(i==rightIndex){
                v.setText("rightVar");
            }else{
                v.setText("var");
            }
            v.setId((long)i);
            variants.add(v);
        }
        res.setQuestionText(name);
        res.setVariants(variants);
        Answer ra = new Answer();
        ra.setClosedAnswer(List.of(variants.get(rightIndex)));
        res.setRightAnswer(ra);
        res.setId(rand.nextLong());
        res.setQuestionType(QuestionType.OneVarQuestion);

        return res;

    }

    Question generateMulVarQuestion(String name, int right, int wrong){

        Random rand = new Random();
        List<Variant> rVars = new ArrayList<>();
        List<Variant> wVars = new ArrayList<>();

        for(int i = 0; i<right+wrong; i++){
            Variant v = new Variant();
            v.setId((long)i);
            if(i<right){
                v.setText("rightVar" + i);
                rVars.add(v);
            }else{
                v.setText("Var" + i);
                wVars.add(v);
            }
        }

        wVars.addAll(rVars);
        Collections.shuffle(wVars);

        Answer a = new Answer();
        a.setClosedAnswer(rVars);

        Question q = new Question();
        q.setQuestionText(name);
        q.setVariants(wVars);
        q.setRightAnswer(a);
        q.setId(rand.nextLong());
        q.setQuestionType(QuestionType.MulVarQuestion);
        return q;
    }

    Question generateOrderQuestion(String name, int size){
        Random rand = new Random();
        List<Variant> vars = new ArrayList<>();

        for(int i = 0; i<size; i++){
            Variant v = new Variant();
            v.setId((long)i);
            v.setText("var"+i);
            vars.add(v);
        }

        List<Variant> rightVars = new ArrayList<>(vars);
        Collections.shuffle(vars);

        Answer a = new Answer();
        a.setClosedAnswer(rightVars);

        Question q = new Question();
        q.setQuestionText(name);
        q.setVariants(vars);
        q.setRightAnswer(a);
        q.setId(rand.nextLong());
        q.setQuestionType(QuestionType.OrderQuestion);
        return q;
    }

    Answer getOneVarWrongAnswer(Question q){
        for(Variant v: q.getVariants()){
            if(!q.getRightAnswer().getClosedAnswer().contains(v)){
                Answer a = new Answer();
                a.setClosedAnswer(List.of(v));
                return a;
            }
        }
        return null;
    }

    Answer getMulVarWrongAnswer(Question q, int rightAmount, int wrongAmount){

        List<Variant> res = new ArrayList<>();

        for(Variant qq: q.getVariants()){
            if(q.getRightAnswer().getClosedAnswer().contains(qq) && rightAmount>0){
                res.add(qq);
                rightAmount--;
            }else if(!q.getRightAnswer().getClosedAnswer().contains(qq) && wrongAmount>0){
                res.add(qq);
                wrongAmount--;
            }
        }

        Answer resA = new Answer();
        resA.setClosedAnswer(res);
        return resA;

    }

    Answer getOrderWrongAnswer(Question q, int right, int wrong){
        List<Integer> indexes = new ArrayList<>();
        for(int i = 0; i<right+wrong; i++){
            indexes.add(i);
        }
        Collections.shuffle(indexes);


        List<Variant> answerVars = new ArrayList<>(q.getRightAnswer().getClosedAnswer());

        int len = right + wrong;
        for(int i = 0; i<len; i+=2){
            if(wrong>1){
                int firstInd = indexes.get(0);
                indexes.remove(0);
                int secondInd = indexes.get(0);
                indexes.remove(0);
                Collections.swap(answerVars, firstInd, secondInd);

                //System.out.println(firstInd + " " + secondInd);
                wrong -=2;
            }
        }

        Answer resA = new Answer();
        resA.setClosedAnswer(answerVars);
        return resA;
    }
}
