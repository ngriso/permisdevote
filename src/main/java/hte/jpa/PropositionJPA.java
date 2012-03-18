package hte.jpa;

import hte.voxe.Proposition;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;

@Entity
public class PropositionJPA {

    @Id
    public String id;
    @Column(columnDefinition = "VARCHAR(1000000)")
    public String text;

    @ManyToOne(optional = false)
    public CandidacyJPA candidacy;
    @ManyToMany
    public Set<TagJPA> tags;
    @ManyToOne
    public TagJPA tagLevel1;

    public static void build(CandidacyJPA candidacyJPA, Proposition proposition) {
        PropositionJPA propositionJPA = new PropositionJPA();
        propositionJPA.id = proposition.id;
        propositionJPA.text = proposition.text;
        propositionJPA.candidacy = candidacyJPA;
        propositionJPA.tags = new HashSet<TagJPA>();
        for (hte.voxe.Id tagId : proposition.tags) {
            TagJPA tagJPA = JpaUtil.getEntityManager().find(TagJPA.class, tagId.id);
            if (tagJPA.level == 1) {
                propositionJPA.tagLevel1 = tagJPA;
            }
            propositionJPA.tags.add(tagJPA);
        }
        JpaUtil.save(propositionJPA);
    }
}
