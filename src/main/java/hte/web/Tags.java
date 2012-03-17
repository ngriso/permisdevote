package hte.web;

import hte.jpa.JpaUtil;
import hte.jpa.TagJPA;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("tags")
@Produces(MediaType.APPLICATION_JSON)
public class Tags {
	
	 @GET	
	 public List<TagJPA> getAll() {
		 return JpaUtil.getAllFrom(TagJPA.class);
	 }

}
