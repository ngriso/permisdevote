package hte.web;

import hte.jpa.CandidacyJPA;
import hte.jpa.CandidateJPA;
import hte.jpa.JpaUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("candidacies")
@Produces(MediaType.APPLICATION_JSON)
public class Candidacies {

    @GET
    public List<CandidacyJPA> getAll() {
    	List<CandidacyJPA> result = JpaUtil.getAllFrom(CandidacyJPA.class);
    	Collections.sort(result, new Comparator<CandidacyJPA>() {
    		@Override
    		public int compare(hte.jpa.CandidacyJPA o1, hte.jpa.CandidacyJPA o2) {
    			String lastname1 = ((CandidateJPA) o1.candidates.toArray()[0]).lastName;
    			String lastname2 = ((CandidateJPA) o2.candidates.toArray()[0]).lastName;
    			return lastname1.compareTo(lastname2);
    		}
		});
    	return result;
    }
    
}
