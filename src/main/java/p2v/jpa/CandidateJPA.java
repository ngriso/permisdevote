package p2v.jpa;

import p2v.voxe.Candidate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CandidateJPA {

    @Id
    public String id;
    public String firstName;
    public String lastName;

    public static CandidateJPA build(Candidate candidate, CandidacyJPA candidacyJPA) {
        CandidateJPA candidateJPA = new CandidateJPA();
        candidateJPA.id = candidate.id;
        candidateJPA.firstName = candidate.firstName;
        candidateJPA.lastName = candidate.lastName;
        candidacyJPA.addCandidate(candidateJPA);
        JpaUtil.save(candidateJPA);
        return candidateJPA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CandidateJPA that = (CandidateJPA) o;
        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
