package hte.voxe;

import java.util.List;

public class Election extends VoxeResource {
    public String name;
    public boolean published;
    public List<Candidacy> candidacies;
    public List<OrderedTag> tags;

    public static class OrderedTag extends Tag{
        public Integer position;
        public List<OrderedTag> tags;
    }
}
