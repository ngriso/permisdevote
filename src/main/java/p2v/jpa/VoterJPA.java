package p2v.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class VoterJPA {

    @Id
    public String id;

    public String username;

    public static VoterJPA build(VoterJPA dto) {
        VoterJPA voterJPA = new VoterJPA();
        voterJPA.id = UUID.randomUUID().toString();
        voterJPA.username = dto.username;
        JpaUtil.save(voterJPA);
        UserStatsJPA.build(voterJPA);
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
