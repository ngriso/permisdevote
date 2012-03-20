package p2v.web;

import p2v.jpa.CandidacyJPA;
import p2v.jpa.GlobalStatsJPA;
import p2v.jpa.JpaUtil;
import p2v.jpa.StatsCandidacyJPA;
import p2v.jpa.StatsThemeJPA;
import p2v.jpa.TagJPA;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class RootResource {

    @GET
    @Path("themes")
    public List<TagJPA> getTagLevel1() {
        return JpaUtil.findThemes();
    }

    @Path("questions")
    public Questions getQuestions() {
        return new Questions();
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

    @Path("voters")
    public Voters voters() {
        return new Voters();
    }

    @Path("init_stats")
    @GET
    public Response initStats() {
        List<GlobalStatsJPA> allGlobalStats = JpaUtil.getAllFrom(GlobalStatsJPA.class);
        if (allGlobalStats.isEmpty()) {
            GlobalStatsJPA globalStats = new GlobalStatsJPA();
            globalStats.statsCandidacy = new ArrayList<StatsCandidacyJPA>();
            for (CandidacyJPA candidacyJPA : JpaUtil.getAllFrom(CandidacyJPA.class)) {
                globalStats.statsCandidacy.add(StatsCandidacyJPA.build(candidacyJPA));
            }
            globalStats.statsTheme = new ArrayList<StatsThemeJPA>();
            for (TagJPA tagJPA : JpaUtil.findThemes()) {
                globalStats.statsTheme.add(StatsThemeJPA.build(tagJPA));
            }
            JpaUtil.save(globalStats);
            return Response.ok().build();
        }
        return Response.ok().build();
    }

    @Path("stats")
    @GET
    public Object getStats() {
        return JpaUtil.getAllFrom(GlobalStatsJPA.class).get(0);
    }
}
