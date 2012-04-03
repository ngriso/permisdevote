package p2v.web;

import com.sun.jersey.api.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import p2v.jpa.CandidacyJPA;
import p2v.jpa.JpaUtil;
import p2v.jpa.QuestionJPA;
import p2v.jpa.ResponseJPA;
import p2v.jpa.VoterJPA;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
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

    /**
     * @param questionId question id
     * @param type String - candidacyId ou tagId 
     * @param answer String
     * @param request     request
     * @return boolean true if the answer if correct
     */
    @Path("{questionId}/answer")
    @GET
    public Answer answer(@PathParam("questionId") Long questionId,
    		@QueryParam("type") String type,
    		@QueryParam("answer") String answer, 
    		@Context HttpServletRequest request) {
        VoterJPA voter = (VoterJPA) RootResource.getSession(request).getAttribute(RootResource.KEY_FOR_VOTER);
        QuestionJPA questionJPA = JpaUtil.findById(QuestionJPA.class, questionId);
        ResponseJPA response = voter.answer(questionJPA, type, answer);
        return new Answer(response.correct, questionJPA.rightAnswer, questionJPA.candidacyCorrect);
    }

    public static class Answer {
        public boolean correct;
        public boolean response;
        public CandidacyJPA candidacyJPA;

        public Answer(boolean correct, boolean response, CandidacyJPA candidacyJPA) {
            this.correct = correct;
            this.response = response;
            this.candidacyJPA = candidacyJPA;
        }
    }
}
