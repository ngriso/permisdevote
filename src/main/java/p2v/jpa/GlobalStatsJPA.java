package p2v.jpa;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import p2v.web.Questions;

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
    
    /**
     * Mise à jour des statistiques globales en tenant compte du type de réponse.
     * @param response ResponseJPA
     * @param type String - candidacyId ou tagId 
     */
    public static void newAnswer(ResponseJPA response, String type) {
    	   GlobalStatsJPA globalStats = JpaUtil.getAllFrom(GlobalStatsJPA.class).get(0);
           globalStats.update(response, type);
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
    
    /**
     * Mise à jour des statistiques, soit ceux concernant les candidats, ou les thèmes.
     * @param response ResponseJPA
     * @param type String - candidacyId ou tagId 
     */
    public void update(ResponseJPA response, String type) {
    	if (response != null && StringUtils.isNotEmpty(type)) {
    		StatsCandidacyJPA statsCandidacy = null;
    		StatsThemeJPA statsTheme = null;
    		if (Questions.CANDIDACY_ID.equals(type)) {
    			statsCandidacy = this.getOrCreateStatsForCandidacy(response.question.candidacy);
    			statsCandidacy.answered++;
    		} else if (Questions.THEME_ID.equals(type)) {
    			statsTheme = this.getOrCreateStatsForTheme(response.question.tagLevel1);
    			statsTheme.answered++;
    		}
    		if (response.correct) {
    			if (Questions.CANDIDACY_ID.equals(type)) {
    				statsCandidacy.rights++;		
    			} else if (Questions.THEME_ID.equals(type)) {
    				statsTheme.rights++;		
    			}	
    		}
    		JpaUtil.update(this);
    	}
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
