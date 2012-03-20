package p2v.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ResponseJPA {

    @Id
    @GeneratedValue
    public Long id;

    @OneToOne
    public VoterJPA voter;

    @OneToOne
    public QuestionJPA question;

    public int occurence;
    public boolean correct;

    public void newAnswer(String answer) {
        this.occurence++;
        this.correct = Boolean.valueOf(answer).equals(this.question.rightAnswer);
        JpaUtil.update(this);
    }

    public static ResponseJPA build(VoterJPA voter, QuestionJPA questionJPA, String answer) {
        ResponseJPA responseJPA = new ResponseJPA();
        responseJPA.voter = voter;
        responseJPA.question = questionJPA;
        responseJPA.occurence++;
        responseJPA.correct = Boolean.valueOf(answer).equals(responseJPA.question.rightAnswer);
        JpaUtil.save(responseJPA);
        return responseJPA;
    }
}
