package p2v.jpa;

import p2v.voxe.Tag;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TagJPA {

    @Id
    public String id;
    public String name;
    public String namespace;
    public int level;

    public static TagJPA build(Tag tag) {
        TagJPA tagJPA = new TagJPA();
        tagJPA.id = tag.id;
        tagJPA.name = tag.name;
        tagJPA.namespace = tag.namespace;
        JpaUtil.save(tagJPA);
        return tagJPA;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagJPA tagJPA = (TagJPA) o;
        return !(id != null ? !id.equals(tagJPA.id) : tagJPA.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
