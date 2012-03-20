package p2v.jpa;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class GlobalStatsJPA {

    @JsonIgnore
    @Id
    @GeneratedValue
    public Long id;

    @OneToMany
    public List<StatsCandidacyJPA> statsCandidacy;
    @OneToMany
    public List<StatsThemeJPA> statsTheme;

    public static void newAnswer(ResponseJPA response) {
        GlobalStatsJPA globalStats = JpaUtil.getAllFrom(GlobalStatsJPA.class).get(0);
        globalStats.update(response);
    }

    public void update(ResponseJPA response) {
        StatsCandidacyJPA statsCandidacy = this.getOrCreateStatsForCandidacy(response.question.candidacy);
        StatsThemeJPA statsTheme = this.getOrCreateStatsForTheme(response.question.tagLevel1);
        statsCandidacy.answered++;
        statsTheme.answered++;
        if (response.correct) {
            statsCandidacy.rights++;
            statsTheme.rights++;
        }
        JpaUtil.update(this);
    }

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
