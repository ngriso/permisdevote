package hte.web;

import hte.jpa.CandidateJPA;
import hte.jpa.JpaUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("candidates")
@Produces(MediaType.APPLICATION_JSON)
public class Candidates {

    @GET
    public List<CandidateJPA> getAll() {
        return JpaUtil.getAllFrom(CandidateJPA.class);
    }
}