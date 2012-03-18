package hte.jpa;

import org.apache.commons.lang3.builder.ToStringBuilder;

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

    public boolean rightAnswer;

    @ManyToOne
    public CandidacyJPA candidacy;
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
