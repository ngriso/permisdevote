package hte.web;

import hte.jpa.JpaUtil;
import hte.jpa.ResponseJPA;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("voters")
@Produces(MediaType.APPLICATION_JSON)
public class Voters {

    @GET
    @Path("{username}")
    public List<ResponseJPA> getResponse(@PathParam("username") String username) {
        return JpaUtil.getEntityManager()
                .createQuery("from ResponseJPA where username = :username", ResponseJPA.class)
                .setParameter("username", username)
                .getResultList();
    }
}
