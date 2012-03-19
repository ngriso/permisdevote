package p2v.web;

import com.google.common.base.Preconditions;
import com.sun.jersey.api.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import p2v.jpa.JpaUtil;
import p2v.jpa.QuestionJPA;
import p2v.jpa.ResponseJPA;
import p2v.jpa.StatsCandidacyJPA;
import p2v.jpa.StatsThemeJPA;
import p2v.jpa.UserStatsJPA;

import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
public class Questions {

    @GET
    @Path("all")
    public List<QuestionJPA> getAll() {
        return JpaUtil.getAllFrom(QuestionJPA.class);
    }

    @GET
    @Path("{id}")
    public Boolean answer(@PathParam("id") Long questionId, @QueryParam("answer") String answer, @QueryParam("username") String username) {
        Preconditions.checkArgument(StringUtils.isNotBlank(answer));

        QuestionJPA questionJPA = JpaUtil.getEntityManager().find(QuestionJPA.class, questionId);
        Boolean correct = Boolean.toString(questionJPA.rightAnswer).equalsIgnoreCase(answer);
        try {
            ResponseJPA responseJPA = JpaUtil.getEntityManager()
                    .createQuery("from ResponseJPA where username = :username and question.id = :questionId", ResponseJPA.class)
                    .setParameter("username", username)
                    .setParameter("questionId", questionId)
                    .getSingleResult();
            responseJPA.occurence++;
            responseJPA.correct = correct;
            JpaUtil.update(responseJPA);
        } catch (NoResultException e) {
            ResponseJPA responseJPA = new ResponseJPA();
            responseJPA.username = username;
            responseJPA.question = questionJPA;
            responseJPA.occurence++;
            responseJPA.correct = correct;
            JpaUtil.save(responseJPA);
        }

        try {
            UserStatsJPA userStats = JpaUtil.getEntityManager()
                    .createQuery("from UserStatsJPA where idUser = :idUser", UserStatsJPA.class)
                    .setParameter("idUser", username)
                    .getSingleResult();
            StatsCandidacyJPA statsCandidacy = userStats.getOrCreateStatsForCandidacy(questionJPA.candidacy);
            StatsThemeJPA statsTheme = userStats.getOrCreateStatsForTheme(questionJPA.tagLevel1);
            statsCandidacy.answered++;
            statsTheme.answered ++;
            if (correct) {
                statsCandidacy.rights++;
                statsTheme.rights++;
            }
            JpaUtil.update(userStats);
        } catch (NoResultException e) {
            UserStatsJPA userStats = UserStatsJPA.build(username);
            StatsCandidacyJPA statsCandidacy = userStats.getOrCreateStatsForCandidacy(questionJPA.candidacy);
            StatsThemeJPA statsTheme = userStats.getOrCreateStatsForTheme(questionJPA.tagLevel1);
            statsCandidacy.answered++;
            statsTheme.answered ++;
            if (correct) {
                statsCandidacy.rights++;
                statsTheme.rights++;
            }
            JpaUtil.save(userStats);
        }
        return correct;
    }

    @GET
    public QuestionJPA get(@QueryParam("candidacyId") String candidacyId, @QueryParam("tagId") String tagId) {
        List<Long> listOfIdQuestion = null;
        if (StringUtils.isNotBlank(candidacyId)) {
            listOfIdQuestion = JpaUtil.getEntityManager()
                    .createQuery("select id from QuestionJPA where candidacy.id = :candidacyId", Long.class)
                    .setParameter("candidacyId", candidacyId)
                    .getResultList();
        }

        if (StringUtils.isNotBlank(tagId)) {
            listOfIdQuestion = JpaUtil.getEntityManager()
                    .createQuery("select id from QuestionJPA where tagLevel1.id = :tagId", Long.class)
                    .setParameter("tagId", tagId)
                    .getResultList();
        }

        if (listOfIdQuestion == null || listOfIdQuestion.isEmpty()) {
            throw new NotFoundException();
        }

        Collections.shuffle(listOfIdQuestion);
        return JpaUtil.getEntityManager()
                .createQuery("from QuestionJPA where id = :idChoosed", QuestionJPA.class)
                .setParameter("idChoosed", listOfIdQuestion.get(0))
                .getSingleResult();
    }
}
