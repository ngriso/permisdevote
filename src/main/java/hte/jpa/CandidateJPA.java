package hte.jpa;

import hte.voxe.Candidate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CandidateJPA {

    @Id
    public String id;
    private String firstName;
    public String lastName;

    public static CandidateJPA build(Candidate candidate) {
        CandidateJPA candidateJPA = new CandidateJPA();
        candidateJPA.id = candidate.id;
        candidateJPA.firstName = candidate.firstName;
        candidateJPA.lastName = candidate.lastName;
        JpaUtil.save(candidateJPA);
        return candidateJPA;
    }
}
