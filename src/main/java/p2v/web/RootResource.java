package p2v.web;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import p2v.jpa.CandidacyJPA;
import p2v.jpa.CandidateJPA;
import p2v.jpa.JpaUtil;
import p2v.jpa.TagJPA;
import p2v.jpa.UserStatsJPA;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class RootResource {

    @GET
    @Path("stats/{username}")
    public UserStatsJPA getStats(@PathParam("username") String username) {
        return JpaUtil.getEntityManager()
                .createQuery("from UserStatsJPA where idUser = :idUser", UserStatsJPA.class)
                .setParameter("idUser", username)
                .getSingleResult();
    }

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

    @Path("candidates1")
    @GET
    public Iterable<CandidateJPA> getCandidates1() {
        Iterable<CandidateJPA> candidates1 = Iterables.transform(JpaUtil.getAllFrom(CandidacyJPA.class), new Function<CandidacyJPA, CandidateJPA>() {
            @Override
            public CandidateJPA apply(CandidacyJPA input) {
                return input.candidates.iterator().next();
            }
        });

        List<CandidateJPA> orderCandidates1 = Lists.newArrayList(candidates1);
        Collections.sort(orderCandidates1, new Comparator<CandidateJPA>() {
            @Override
            public int compare(CandidateJPA o1, CandidateJPA o2) {
                return o1.lastName.compareTo(o2.lastName);
            }
        });
        return orderCandidates1;
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
