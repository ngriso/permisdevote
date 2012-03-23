package p2v.web;

import p2v.jpa.JpaUtil;
import p2v.jpa.UserStatsJPA;
import p2v.jpa.VoterJPA;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Voters {

    @POST
    public VoterJPA create(Profile profile) {
        return VoterJPA.build(profile);
    }

    @Path("{userID}/stats")
    @GET
    public UserStatsJPA voter(@PathParam("userID") String userID) {
        VoterJPA voter = JpaUtil.findVoterByUserID(userID);
        return JpaUtil.findUserStatsByVoter(voter);
    }

    public static class Profile {
        public String username;
    }
}
