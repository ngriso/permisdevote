package p2v.jpa;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import p2v.web.Voters;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class VoterJPA {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    public String username;


    public static VoterJPA build(Voters.Profile profile) {
        Preconditions.checkArgument(StringUtils.isNotBlank(profile.username));
        VoterJPA voterJPA = new VoterJPA();
        voterJPA.username = profile.username;
        JpaUtil.save(voterJPA);
        return voterJPA;
    }

    public ResponseJPA answer(QuestionJPA question, String answer) {
        ResponseJPA response = ResponseJPA.build(this, question, answer);
        UserStatsJPA userStats = JpaUtil.findUserStatsByVoter(this);
        userStats.update(response);
        StatsJPA.newAnswer(response);
        return response;
    }
}
