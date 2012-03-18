package hte.web;

import hte.jpa.JpaUtil;
import hte.jpa.TagJPA;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("tags")
@Produces(MediaType.APPLICATION_JSON)
public class Tags {

    @GET
    public List<TagJPA> getAll() {
        return JpaUtil.getAllFrom(TagJPA.class);
    }

    @GET
    @Path("level1")
    public List<TagJPA> getTagLevel1() {
        return JpaUtil.getEntityManager().createQuery("from TagJPA where level = 1 order by name", TagJPA.class).getResultList();
    }
}
