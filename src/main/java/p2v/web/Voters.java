package p2v.web;

import p2v.jpa.JpaUtil;
import p2v.jpa.QuestionJPA;
import p2v.jpa.ResponseJPA;
import p2v.jpa.UserStatsJPA;
import p2v.jpa.VoterJPA;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Voters {

    @POST
    public void create(Profile profile) {
        VoterJPA.build(profile);
    }

    @Path("{username}")
    public Voter voter(@PathParam("username")String username) {
        return new Voter(username);
    }

    public static class Voter {
        private final String username;

        public Voter(String username) {
            this.username = username;
        }

        @Path("answer/{questionId}")
        @GET
        public boolean answer(@PathParam("questionId") Long questionId, @QueryParam("answer") String answer) {
            VoterJPA voter = JpaUtil.findVoterByUsername(username);
            QuestionJPA questionJPA = JpaUtil.findById(QuestionJPA.class, questionId);
            ResponseJPA response = voter.answer(questionJPA, answer);
            return response.correct;
        }

        @Path("stats")
        @GET
        public UserStatsJPA getStats() {
            VoterJPA voter = JpaUtil.findVoterByUsername(username);
            return JpaUtil.findUserStatsByVoter(voter);
        }
    }

    public static class Profile {
        public String username;
    }
}
