package hte.jpa;

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

    @Column(columnDefinition = "VARCHAR(1000000)")
    public String text;

    @JsonIgnore
    public boolean rightAnswer;
    
    @JsonIgnore
    @ManyToOne
    public CandidacyJPA candidacy;
    
    @JsonIgnore
    @ManyToOne
    public TagJPA tagLevel1;

    public static QuestionJPA build(PropositionJPA propositionJPA, CandidacyJPA candidacyJPA) {
        QuestionJPA questionJPA = new QuestionJPA();
        questionJPA.text = propositionJPA.text;
        questionJPA.candidacy = candidacyJPA;
        questionJPA.rightAnswer = propositionJPA.candidacy.id.equals(candidacyJPA.id);
        questionJPA.tagLevel1 = propositionJPA.tagLevel1;
        JpaUtil.save(questionJPA);
        return questionJPA;
    }
}
