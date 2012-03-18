package hte.web;

import hte.jpa.JpaUtil;
import hte.jpa.QuestionJPA;
import hte.jpa.ResponseJPA;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Path("questions")
@Produces(MediaType.APPLICATION_JSON)
public class Questions {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Questions.class);

    @GET
    @Path("all")
    public List<QuestionJPA> getAll() {
        return JpaUtil.getAllFrom(QuestionJPA.class);
    }

    @GET
    @Path("{id}")
    public Boolean answer(@PathParam("id") Long questionId, @QueryParam("answer") String answer, @QueryParam("username") String username) {
    	Boolean result = null;
    	
        QuestionJPA questionJPA = JpaUtil.getEntityManager().find(QuestionJPA.class, questionId);
        if (questionJPA != null && StringUtils.isNotEmpty(answer)) {
        	result = Boolean.toString(questionJPA.rightAnswer).equalsIgnoreCase(answer);
        }

        try {
            ResponseJPA responseJPA = JpaUtil.getEntityManager()
                    .createQuery("from ResponseJPA where username = :username and question.id = :questionId", ResponseJPA.class)
                    .setParameter("username", username)
                    .setParameter("questionId", questionId)
                    .getSingleResult();
            responseJPA.occurence++;
            responseJPA.correct = result;
            JpaUtil.update(responseJPA);
        } catch (NoResultException e) {
            ResponseJPA responseJPA = new ResponseJPA();
            responseJPA.username = username;
            responseJPA.question = questionJPA;
            responseJPA.occurence++;
            responseJPA.correct = result;
            JpaUtil.save(responseJPA);
        }
        return result;
    }

    @GET
    public QuestionJPA get(@QueryParam("candidacyId") String candidacyId, @QueryParam("tagId") String tagId) {
        QuestionJPA result = null;
        try {
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
        
        	if (listOfIdQuestion != null && !listOfIdQuestion.isEmpty()) {
        		Collections.shuffle(listOfIdQuestion);
        		result = JpaUtil.getEntityManager()
        				.createQuery("from QuestionJPA where id = :idChoosed", QuestionJPA.class)
        				.setParameter("idChoosed", listOfIdQuestion.get(0))
        				.getSingleResult();
        	}
        } catch (Exception e) {
        	LOGGER.error(e.getMessage());
        	e.printStackTrace();
		}
        return result;
    }
}
