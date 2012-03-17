package hte.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PropositionJPA {
	
	@Id
	private String id;
	private String text;
	
	

}
