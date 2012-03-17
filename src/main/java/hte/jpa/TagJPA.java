package hte.jpa;

import hte.voxe.Candidate;
import hte.voxe.Tag;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TagJPA {
	
	@Id
	private String id;
	private String name;
	
	public static TagJPA build(Tag tag) {
		TagJPA tagJPA = new TagJPA();
	    tagJPA.id = tag.id;
	    tagJPA.name = tag.name;
	    JpaUtil.save(tagJPA);	
	    return tagJPA;	
	}
	
	
}
