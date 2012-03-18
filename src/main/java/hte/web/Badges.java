package hte.web;

import hte.jpa.CandidacyJPA;
import hte.jpa.JpaUtil;
import hte.jpa.ResponseJPA;
import hte.jpa.TagJPA;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("badges")
@Produces(MediaType.APPLICATION_JSON)
public class Badges {

    @Path("{username}")
    @GET
    public Badge getMyBadges(@PathParam("username") String username) {

        List<ResponseJPA> myResponse = JpaUtil.getEntityManager()
                .createQuery("from ResponseJPA where username = :username", ResponseJPA.class)
                .setParameter("username", username)
                .getResultList();

        Map<CandidacyJPA, Score> candidacyScoreMap = new HashMap<CandidacyJPA, Score>();
        Map<TagJPA, Score> tagScoreMap = new HashMap<TagJPA, Score>();
        for (ResponseJPA response : myResponse) {
            CandidacyJPA candidacy = response.question.candidacy;
            TagJPA tagLevel1 = response.question.tagLevel1;

            Score scoreCandidat = candidacyScoreMap.get(candidacy);
            if (scoreCandidat == null) {
                scoreCandidat = new Score(1, response.correct ? 1 : 0);
                candidacyScoreMap.put(candidacy, scoreCandidat);
            } else {
                scoreCandidat.answered++;
                if (response.correct) {
                    scoreCandidat.rights++;
                }
            }

            Score scoreTag = tagScoreMap.get(tagLevel1);
            if (scoreTag == null) {
                scoreTag = new Score(1, response.correct ? 1 : 0);
                tagScoreMap.put(tagLevel1, scoreTag);
            } else {
                scoreTag.answered++;
                if (response.correct) {
                    scoreTag.rights++;
                }
            }
        }

        return new Badge(candidacyScoreMap, tagScoreMap);
    }

    public static class Badge {
        Map<CandidacyJPA, Score> candidacyScoreMap;
        Map<TagJPA, Score> tagScoreMap;

        public Badge(Map<CandidacyJPA, Score> candidacyScoreMap, Map<TagJPA, Score> tagScoreMap) {
            this.candidacyScoreMap = candidacyScoreMap;
            this.tagScoreMap = tagScoreMap;
        }
    }

    public static class Score {
        public int answered;
        public int rights;

        public Score(int answered, int rights) {
            this.answered = answered;
            this.rights = rights;
        }
    }
}
