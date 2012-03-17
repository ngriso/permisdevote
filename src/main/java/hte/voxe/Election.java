package hte.voxe;

import java.util.List;

public class Election extends VoxeResource {
    public String name;
    public boolean published;
    public List<Candidacy> candidacies;
}
