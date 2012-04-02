package p2v.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
public class VoterJPA {

    @Id
    public String id;
    public String username;
    public String activity;
    public int age;

    public static VoterJPA build(VoterJPA dto) {
        VoterJPA voterJPA = new VoterJPA();
        voterJPA.id = UUID.randomUUID().toString();
        voterJPA.username = dto.username;
        voterJPA.activity = dto.activity;
        voterJPA.age = dto.age;
        JpaUtil.save(voterJPA);
        UserStatsJPA.build(voterJPA);
        return voterJPA;
    }

    /**
     * @param question QuestionJPA
     * @param type     String - candidacyId ou tagId
     * @param answer   String
     * @return ResponseJPA
     */
    public ResponseJPA answer(QuestionJPA question, String type, String answer) {
        ResponseJPA response = ResponseJPA.build(this, question, answer);
        UserStatsJPA userStats = JpaUtil.findUserStatsByVoter(this);
        userStats.update(response, type);
        StatsJPA.newAnswer(response, type);
        return response;
    }

    public static Map<String, String> activities = new HashMap<String, String>() {{
        final String Etudiant = "Etudiant";
        final String Fonctionnaire = "Fonctionnaire";
        final String Retraité = "Retraité";
        final String SansEmploi = "Sans emploi";
        final String Agriculteur = "Agriculteur";
        final String Cadres = "Cadres";

    }};

    public static interface Ages {
        final int entre_18_25 = 1;
        final int entre_26_35 = 2;
        final int entre_36_45 = 3;
        final int entre_46_55 = 4;
        final int entre_56_65 = 5;
        final int plus_66 = 6;
    }
}
