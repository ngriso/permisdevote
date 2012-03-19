package p2v.web;

import p2v.jpa.CandidacyJPA;
import p2v.jpa.CandidateJPA;
import p2v.jpa.JpaUtil;
import p2v.jpa.TagJPA;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class RootResource {

    @Path("badges")
    public Badges getBadges() {
        return new Badges();
    }

    @Path("tags")
    @GET
    public List<TagJPA> getAll() {
        return JpaUtil.getAllFrom(TagJPA.class);
    }

    @GET
    @Path("tags/level1")
    public List<TagJPA> getTagLevel1() {
        return JpaUtil.getEntityManager().createQuery("from TagJPA where level = 1 order by name", TagJPA.class).getResultList();
    }

    @Path("questions")
    public Questions getQuestions() {
        return new Questions();
    }

    @Path("candidates")
    @GET
    public List<CandidateJPA> getCandidates() {
        return JpaUtil.getAllFrom(CandidateJPA.class);
    }

    @Path("candidacies")
    @GET
    public List<CandidacyJPA> getCandidacies() {
        List<CandidacyJPA> result = JpaUtil.getAllFrom(CandidacyJPA.class);
        Collections.sort(result, new Comparator<CandidacyJPA>() {
            @Override
            public int compare(p2v.jpa.CandidacyJPA o1, p2v.jpa.CandidacyJPA o2) {
                String lastname1 = ((CandidateJPA) o1.candidates.toArray()[0]).lastName;
                String lastname2 = ((CandidateJPA) o2.candidates.toArray()[0]).lastName;
                return lastname1.compareTo(lastname2);
            }
        });
        return result;
    }

}
