package p2v.jpa;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import p2v.web.Voters;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NoResultException;

@Entity
public class VoterJPA {
	
	@Id
    @GeneratedValue
	private Long id;
	
    public String username;


    public static VoterJPA build(Voters.Profile profile) {
        Preconditions.checkArgument(StringUtils.isNotBlank(profile.username));
        VoterJPA voterJPA = new VoterJPA();
        voterJPA.username = profile.username;
        return voterJPA;
    }

    public ResponseJPA answer(QuestionJPA question, String answer) {
        ResponseJPA response;
        try {
            response = JpaUtil.findResponseByVoterAndQuestion(question.id, this);
            response.newAnswer(answer);
        } catch (NoResultException e) {
            response = ResponseJPA.build(this, question, answer);
        }

        try {
            UserStatsJPA userStats = JpaUtil.findUserStatsByVoter(this);
            userStats.update(response);
        } catch (NoResultException e) {
            UserStatsJPA userStats = UserStatsJPA.build(this);
            userStats.update(response);
        }

        GlobalStatsJPA.newAnswer(response);

        return response;
    }
}
