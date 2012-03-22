package p2v.jpa;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class UserStatsJPA extends StatsJPA {

    @JsonIgnore
    @ManyToOne
    public VoterJPA voter;

    public static UserStatsJPA build(VoterJPA voter) {
        UserStatsJPA userStats = new UserStatsJPA();
        userStats.initialize();
        userStats.voter = voter;
        JpaUtil.save(userStats);
        return userStats;
    }
}
