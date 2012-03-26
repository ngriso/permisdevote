package p2v.jpa;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.ArrayList;

@Entity
public class UserStatsJPA extends GlobalStatsJPA {

    @JsonIgnore
    @ManyToOne
    public VoterJPA voter;

    public static UserStatsJPA build(VoterJPA voter) {
        UserStatsJPA userStats = new UserStatsJPA();
        userStats.voter = voter;
        userStats.statsCandidacy = new ArrayList<StatsCandidacyJPA>();
        for (CandidacyJPA candidacyJPA : JpaUtil.getAllFrom(CandidacyJPA.class)) {
            userStats.statsCandidacy.add(StatsCandidacyJPA.build(candidacyJPA));
        }
        userStats.statsTheme = new ArrayList<StatsThemeJPA>();
        for (TagJPA tagJPA : JpaUtil.findThemes()) {
            userStats.statsTheme.add(StatsThemeJPA.build(tagJPA));
        }
        JpaUtil.save(userStats);
        return userStats;
    }

	
}
