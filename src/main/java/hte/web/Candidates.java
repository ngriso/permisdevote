package hte.web;

import hte.jpa.CandidateJPA;
import hte.jpa.JpaUtil;
import hte.voxe.Candidate;
import hte.voxe.Proposition;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("candidates")
@Produces(MediaType.APPLICATION_JSON)
public class Candidates {
	
	 @GET	
	 public List<CandidateJPA> getAll() {
		 return JpaUtil.getAllFrom(CandidateJPA.class);
	 }
	 
	 
	 
	 
	
	

}
