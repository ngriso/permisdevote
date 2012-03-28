package p2v.web;

import com.sun.jersey.api.NotFoundException;
import org.apache.commons.lang3.StringUtils;
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

    @Path("next")
    @GET
    public QuestionJPA get(@QueryParam("candidacyId") String candidacyId, @QueryParam("tagId") String tagId) {
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
    public boolean answer(@PathParam("questionId") Long questionId, @QueryParam("answer") String answer, @Context HttpServletRequest request) {
        VoterJPA voter = (VoterJPA) RootResource.getSession(request).getAttribute(RootResource.KEY_FOR_VOTER);
        QuestionJPA questionJPA = JpaUtil.findById(QuestionJPA.class, questionId);
        ResponseJPA response = voter.answer(questionJPA, answer);
        return response.correct;
    }

}
