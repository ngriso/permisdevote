package p2v.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StatsThemeJPA {

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    public TagJPA tag;

    public int answered;
    public int rights;

    public static StatsThemeJPA build(TagJPA theme) {
        StatsThemeJPA statsThemeJPA = new StatsThemeJPA();
        statsThemeJPA.tag = theme;
        statsThemeJPA.answered = 0;
        statsThemeJPA.rights = 0;
        JpaUtil.save(statsThemeJPA);
        return statsThemeJPA;
    }
}
