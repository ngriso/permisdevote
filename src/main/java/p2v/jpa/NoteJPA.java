package p2v.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class NoteJPA {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    public VoterJPA voter;

    @ManyToOne
    public QuestionJPA question;

    public int note;

    public static final int ILIKE = 10;
    public static final int IDONTLIKE = 0;

}
