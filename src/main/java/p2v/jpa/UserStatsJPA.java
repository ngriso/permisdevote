package p2v.jpa;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserStatsJPA {

    @JsonIgnore
    @Id
    @GeneratedValue
    public Long id;

    @JsonIgnore
    public String idUser;

    @OneToMany
    public List<StatsCandidacyJPA> statsCandidacy;
    @OneToMany
    public List<StatsThemeJPA> statsTheme;

    public StatsCandidacyJPA getOrCreateStatsForCandidacy(CandidacyJPA candidacy) {
        for (StatsCandidacyJPA statsCandidacyJPA : statsCandidacy) {
            if (statsCandidacyJPA.candidacy.equals(candidacy)) {
                return statsCandidacyJPA;
            }
        }
        StatsCandidacyJPA statsCandidacyJPA = StatsCandidacyJPA.build(candidacy);
        statsCandidacy.add(statsCandidacyJPA);
        return statsCandidacyJPA;
    }

    public static UserStatsJPA build(String username) {
        UserStatsJPA userStats = new UserStatsJPA();
        userStats.idUser = username;
        userStats.statsCandidacy = new ArrayList<StatsCandidacyJPA>();
        userStats.statsTheme = new ArrayList<StatsThemeJPA>();
        return userStats;
    }

    public StatsThemeJPA getOrCreateStatsForTheme(TagJPA theme) {
        for (StatsThemeJPA statsThemeJPA : statsTheme) {
            if (statsThemeJPA.tag.equals(theme)) {
                return statsThemeJPA;
            }
        }
        StatsThemeJPA statsThemeJPA = StatsThemeJPA.build(theme);
        this.statsTheme.add(statsThemeJPA);
        return statsThemeJPA;
    }
}
