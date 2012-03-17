package hte.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CandidateJPA {
	
	@Id
	private String id;
	private String firstName;
    private String lastName;

}
