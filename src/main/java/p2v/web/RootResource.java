package p2v.web;

import p2v.jpa.CandidacyJPA;
import p2v.jpa.StatsJPA;
import p2v.jpa.JpaUtil;
import p2v.jpa.TagJPA;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class RootResource {

    @Path("questions")
    public Questions getQuestions() {
        return new Questions();
    }

    @Path("voters")
    public Voters voters() {
        return new Voters();
    }

    @GET
    @Path("themes")
    public List<TagJPA> getTagLevel1() {
        return JpaUtil.findThemes();
    }

    @Path("candidacies")
    @GET
    public List<CandidacyJPA> getCandidacies() {
        List<CandidacyJPA> result = JpaUtil.getAllFrom(CandidacyJPA.class);
        Collections.sort(result, new Comparator<CandidacyJPA>() {
            @Override
            public int compare(p2v.jpa.CandidacyJPA o1, p2v.jpa.CandidacyJPA o2) {
                String lastname1 = o1.candidate1.lastName;
                String lastname2 = o2.candidate1.lastName;
                return lastname1.compareTo(lastname2);
            }
        });
        return result;
    }

    @Path("init_stats")
    @GET
    public Response initStats() {
        List<StatsJPA> allStats = JpaUtil.getAllFrom(StatsJPA.class);
        if (allStats.isEmpty()) {
            StatsJPA stats = new StatsJPA();
            stats.initialize();
            JpaUtil.save(stats);
            return Response.ok().build();
        }
        return Response.ok().build();
    }

    @Path("stats")
    @GET
    public StatsJPA getStats() {
        return JpaUtil.getAllFrom(StatsJPA.class).get(0);
    }
}
