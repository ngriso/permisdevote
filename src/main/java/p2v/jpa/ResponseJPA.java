package p2v.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
public class ResponseJPA {

    @Id
    @GeneratedValue
    public Long id;

    @OneToOne
    public VoterJPA voter;

    @OneToOne
    public QuestionJPA question;

    public Date creationDate;
    public boolean correct;

    public static ResponseJPA build(VoterJPA voter, QuestionJPA questionJPA, String answer) {
        ResponseJPA responseJPA = new ResponseJPA();
        responseJPA.voter = voter;
        responseJPA.question = questionJPA;
        responseJPA.creationDate = new Date();
        responseJPA.correct = Boolean.valueOf(answer).equals(responseJPA.question.rightAnswer);
        JpaUtil.save(responseJPA);
        return responseJPA;
    }
}
