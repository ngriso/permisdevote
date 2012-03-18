package hte.web;

import hte.jpa.JpaUtil;
import hte.jpa.QuestionJPA;
import hte.jpa.ResponseJPA;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Path("questions")
@Produces(MediaType.APPLICATION_JSON)
public class Questions {

    @GET
    @Path("all")
    public List<QuestionJPA> getAll() {
        return JpaUtil.getAllFrom(QuestionJPA.class);
    }

    @Path("{id}")
    @POST
    public void answer(@PathParam("id") Long questionId, @QueryParam("answer") String answer, @QueryParam("username") String username) {
        QuestionJPA questionJPA = JpaUtil.getEntityManager().find(QuestionJPA.class, questionId);

        try {
            ResponseJPA responseJPA = JpaUtil.getEntityManager()
                    .createQuery("from ResponseJPA where username = :username and question.id = :questionId", ResponseJPA.class)
                    .setParameter("username", username)
                    .setParameter("questionId", questionId)
                    .getSingleResult();
            responseJPA.occurence++;
            responseJPA.correct = Boolean.toString(questionJPA.rightAnswer).equalsIgnoreCase(answer);
            JpaUtil.update(responseJPA);
        } catch (NoResultException e) {
            ResponseJPA responseJPA = new ResponseJPA();
            responseJPA.username = username;
            responseJPA.question = questionJPA;
            responseJPA.occurence++;
            responseJPA.correct = Boolean.toString(questionJPA.rightAnswer).equalsIgnoreCase(answer);
            JpaUtil.save(responseJPA);
        }
    }

    @GET
    public QuestionJPA get(@QueryParam("candidacyId") String candidacyId, @QueryParam("tagId") String tagId) {
        QuestionJPA result = null;
        
    	List<QuestionJPA> questionJPAs = new ArrayList<QuestionJPA>();
        if (StringUtils.isNotBlank(candidacyId)) {
            questionJPAs = JpaUtil.getEntityManager()
                    .createQuery("from QuestionJPA where candidacy.id = :candidacyId", QuestionJPA.class)
                    .setParameter("candidacyId", candidacyId)
                    .getResultList();
        }

        if (StringUtils.isNotBlank(tagId)) {
            questionJPAs = JpaUtil.getEntityManager()
                    .createQuery("from QuestionJPA where tagLevel1.id = :tagId", QuestionJPA.class)
                    .setParameter("tagId", tagId)
                    .getResultList();
        }
        
        if (questionJPAs != null && !questionJPAs.isEmpty()) {
        	Random random = new Random(questionJPAs.size());
        	long idChoosed = random.nextLong();
        	result = JpaUtil.getEntityManager()
        			.createQuery("from QuestionJPA where id = :idChoosed", QuestionJPA.class)
                    .setParameter("id", idChoosed)
                    .getSingleResult();
        }

        return result;
    }
}
