package p2v.web;

import com.sun.jersey.api.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import p2v.jpa.JpaUtil;
import p2v.jpa.QuestionJPA;
import p2v.jpa.ResponseJPA;
import p2v.jpa.VoterJPA;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Questions {
	
	public static final String CANDIDACY_ID = "candidacyId";
	public static final String THEME_ID = "tagId"; 

    @Path("next")
    @GET
    public QuestionJPA get(@QueryParam(CANDIDACY_ID) String candidacyId, @QueryParam(THEME_ID) String tagId) {
        List<Long> listOfIdQuestion = null;
        if (StringUtils.isNotBlank(candidacyId)) {
            listOfIdQuestion = JpaUtil.findQuestionsIdByCandidacyId(candidacyId);
        }

        if (listOfIdQuestion == null && StringUtils.isNotBlank(tagId)) {
            listOfIdQuestion = JpaUtil.findQuestionsIdByTagId(tagId);
        }

        if (listOfIdQuestion == null || listOfIdQuestion.isEmpty()) {
            throw new NotFoundException();
        }

        Collections.shuffle(listOfIdQuestion);
        return JpaUtil.findById(QuestionJPA.class, listOfIdQuestion.get(0));
    }

    @Path("{questionId}/answer")
    @GET
    /**
     * 
     * @param questionId
     * @param type String - candidacyId ou tagId 
     * @param answer String
     * @param username String
     * @return boolean
     */
    public boolean answer(@PathParam("questionId") Long questionId,
    		@QueryParam("type") String type,
    		@QueryParam("answer") String answer, 
    		@QueryParam("username") String username) {
        VoterJPA voter = JpaUtil.findVoterByUsername(username);
        QuestionJPA questionJPA = JpaUtil.findById(QuestionJPA.class, questionId);
        ResponseJPA response = voter.answer(questionJPA, type, answer);
        return response.correct;
    }

}
