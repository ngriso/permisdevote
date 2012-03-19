package p2v.voxe;

import java.util.List;

public class VoxeResponses {
    public static class ResponseElection extends AbstractVoxeResponse {
        public Response response;
        public static class Response {
            public Election election;
        }
    }

    public static class ResponseTags extends AbstractVoxeResponse {
        public List<Tag> response;
    }

    public static class ResponsePropositions extends AbstractVoxeResponse {
        public Response response;
        public static class Response {
            public List<Proposition> propositions;
        }
    }

    public static class AbstractVoxeResponse {
        public Meta meta;
        public static class Meta {
            public String code;
        }
    }
}
