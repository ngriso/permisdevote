package hte.jpa;

import hte.voxe.Candidacy;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CandidacyJPA {

    @Id
    public String id;
    @OneToMany
    public Set<CandidateJPA> candidates = new HashSet<CandidateJPA>();

    public static CandidacyJPA build(Candidacy candidacy) {
        CandidacyJPA candidacyJPA = new CandidacyJPA();
        candidacyJPA.id = candidacy.id;
        return candidacyJPA;
    }

    @Override
    public String toString() {
        return id;
    }
}
