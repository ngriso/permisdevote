package hte.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TagJPA {
	
	@Id
	private String id;
	private String name;

}
