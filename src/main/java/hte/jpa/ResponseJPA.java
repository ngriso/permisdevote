package hte.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class ResponseJPA {

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    public ElecteurJPA electeur;

    @OneToOne
    public QuestionJPA question;

    public int occurence;
    public boolean correct;
    public String username;
}
