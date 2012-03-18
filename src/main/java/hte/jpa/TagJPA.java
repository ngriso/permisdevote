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
	private String namespace;
    public int level;

    public static TagJPA build(Tag tag) {
		TagJPA tagJPA = new TagJPA();
	    tagJPA.id = tag.id;
	    tagJPA.name = tag.name;
	    tagJPA.namespace = tag.namespace;
	    JpaUtil.save(tagJPA);	
	    return tagJPA;	
	}
}
