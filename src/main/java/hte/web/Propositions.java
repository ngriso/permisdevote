package hte.web;

import hte.jpa.JpaUtil;
import hte.jpa.PropositionJPA;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("propositions")
@Produces(MediaType.APPLICATION_JSON)
public class Propositions {
    
    @GET
    public List<PropositionJPA> get() {
        return JpaUtil.getAllFrom(PropositionJPA.class);
    }
}
