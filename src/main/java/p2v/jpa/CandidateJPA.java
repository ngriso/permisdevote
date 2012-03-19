package p2v.jpa;

import p2v.voxe.Candidate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CandidateJPA {

    @Id
    public String id;
    private String firstName;
    public String lastName;

    @ManyToOne
    public CandidacyJPA candidacy;

    public static CandidateJPA build(Candidate candidate, CandidacyJPA candidacyJPA) {
        CandidateJPA candidateJPA = new CandidateJPA();
        candidateJPA.id = candidate.id;
        candidateJPA.firstName = candidate.firstName;
        candidateJPA.lastName = candidate.lastName;
        candidateJPA.candidacy = candidacyJPA;
        JpaUtil.save(candidateJPA);
        return candidateJPA;
    }
}
