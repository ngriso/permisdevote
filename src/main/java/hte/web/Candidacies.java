package hte.web;

import hte.jpa.CandidacyJPA;
import hte.jpa.JpaUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("candidacies")
@Produces(MediaType.APPLICATION_JSON)
public class Candidacies {

    @GET
    public List<CandidacyJPA> getAll() {
        return JpaUtil.getAllFrom(CandidacyJPA.class);
    }
}
