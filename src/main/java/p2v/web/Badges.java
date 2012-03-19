package p2v.web;

import p2v.jpa.CandidacyJPA;
import p2v.jpa.CandidateJPA;
import p2v.jpa.JpaUtil;
import p2v.jpa.ResponseJPA;
import p2v.jpa.TagJPA;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Produces(MediaType.APPLICATION_JSON)
public class Badges {

    @Path("{username}")
    @GET
    public Badge getMyBadges(@PathParam("username") String username) {

        List<CandidacyJPA> candidacies = JpaUtil.getAllFrom(CandidacyJPA.class);
        List<TagJPA> tags = JpaUtil.getEntityManager().createQuery("from TagJPA where level = 1", TagJPA.class).getResultList();

        List<ResponseJPA> myResponse = JpaUtil.getEntityManager()
                .createQuery("from ResponseJPA where username = :username", ResponseJPA.class)
                .setParameter("username", username)
                .getResultList();

        Map<CandidacyJPA, Score> candidacyScoreMap = new HashMap<CandidacyJPA, Score>();
        for (CandidacyJPA candidacy : candidacies) {
            candidacyScoreMap.put(candidacy, new Score(0, 0));
        }

        Map<TagJPA, Score> tagScoreMap = new HashMap<TagJPA, Score>();
        for (TagJPA tag : tags) {
            tagScoreMap.put(tag, new Score(0, 0));
        }

        for (ResponseJPA response : myResponse) {
            CandidacyJPA candidacy = response.question.candidacy;
            TagJPA tagLevel1 = response.question.tagLevel1;

            Score scoreCandidat = candidacyScoreMap.get(candidacy);
            scoreCandidat.answered++;
            if (response.correct) {
                scoreCandidat.rights++;
            }

            Score scoreTag = tagScoreMap.get(tagLevel1);
            scoreTag.answered++;
            if (response.correct) {
                scoreTag.rights++;
            }
        }

        List<BadgeCandidat> badgesCandidats = new ArrayList<BadgeCandidat>();
        for (Map.Entry<CandidacyJPA, Score> entry : candidacyScoreMap.entrySet()) {
            badgesCandidats.add(new BadgeCandidat(entry.getKey(), entry.getValue().answered, entry.getValue().rights));
        }

        List<BadgeTheme> badgeThemes = new ArrayList<BadgeTheme>();
        for (Map.Entry<TagJPA, Score> entry : tagScoreMap.entrySet()) {
            badgeThemes.add(new BadgeTheme(entry.getKey(), entry.getValue().answered, entry.getValue().rights));
        }

        Collections.sort(badgesCandidats, new Comparator<BadgeCandidat>() {
            @Override
            public int compare(BadgeCandidat o1, BadgeCandidat o2) {
    			String lastname1 = ((CandidateJPA) o1.candidacy.candidates.toArray()[0]).lastName;
    			String lastname2 = ((CandidateJPA) o2.candidacy.candidates.toArray()[0]).lastName;
                return lastname1.compareTo(lastname2);
            }
        });
        Collections.sort(badgeThemes, new Comparator<BadgeTheme>() {
            @Override
            public int compare(BadgeTheme o1, BadgeTheme o2) {
                return o1.theme.name.compareTo(o2.theme.name);
            }
        });
        return new Badge(badgesCandidats, badgeThemes);
    }

    public static class Badge {
        public List<BadgeCandidat> badgesCandidats;
        public List<BadgeTheme> badgeThemes;

        public Badge(List<BadgeCandidat> badgesCandidats, List<BadgeTheme> badgeThemes) {
            this.badgesCandidats = badgesCandidats;
            this.badgeThemes = badgeThemes;
        }
    }

    public static class BadgeCandidat {
        public CandidacyJPA candidacy;
        public int answered;
        public int rights;

        public BadgeCandidat(CandidacyJPA candidacy, int answered, int rights) {
            this.candidacy = candidacy;
            this.answered = answered;
            this.rights = rights;
        }
    }

    public static class BadgeTheme {
        public TagJPA theme;
        public int answered;
        public int rights;

        public BadgeTheme(TagJPA theme, int answered, int rights) {
            this.theme = theme;
            this.answered = answered;
            this.rights = rights;
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
