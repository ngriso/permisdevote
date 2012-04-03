package p2v.jpa;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class QuestionJPA {

    @Id
    @GeneratedValue
    public Long id;

    @Column(columnDefinition = "LONGVARCHAR")
    public String text;

    @JsonIgnore
    public boolean rightAnswer;
    
    @ManyToOne
    public CandidacyJPA candidacy;

    @ManyToOne
    public CandidacyJPA candidacyCorrect;

    @ManyToOne
    public TagJPA tagLevel1;

    public static QuestionJPA build(PropositionJPA propositionJPA, CandidacyJPA candidacyJPA, CandidacyJPA candidacyCorrect) {
        QuestionJPA questionJPA = new QuestionJPA();
        questionJPA.text = propositionJPA.text;
        questionJPA.candidacy = candidacyJPA;
        questionJPA.candidacyCorrect = candidacyCorrect;
        questionJPA.rightAnswer = propositionJPA.candidacy.id.equals(candidacyJPA.id);
        questionJPA.tagLevel1 = propositionJPA.tagLevel1;
        JpaUtil.save(questionJPA);
        return questionJPA;
    }
}
