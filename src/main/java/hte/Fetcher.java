package hte;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import hte.jersey.ContextResolverForObjectMapper;
import hte.jpa.CandidateJPA;
import hte.jpa.TagJPA;
import hte.voxe.Candidacy;
import hte.voxe.Candidate;
import hte.voxe.Election;
import hte.voxe.Proposition;
import hte.voxe.Tag;
import hte.voxe.VoxeResponses;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("fetch")
@Produces(MediaType.APPLICATION_JSON)
public class Fetcher {

    private Client client = buildClient();

    @GET
    @Path("candidates")
    public Response fetchCandidates() {
        String election2012URL = "http://voxe.org/api/v1/elections/4f16fe2299c7a10001000012";

        Election election = client.resource(election2012URL).get(VoxeResponses.ResponseElection.class).response.election;

        List<CandidateJPA> candidates = new ArrayList<CandidateJPA>();
        for (Candidacy candidacy : election.candidacies) {
            //TODO on considere un candidat par candidature
            Candidate candidate = candidacy.candidates.get(0);
            candidates.add(CandidateJPA.build(candidate));
        }

        return Response.ok().build();
    }

    @GET
    @Path("2012")
    public Map<Candidate, List<Proposition>> fetchElection() {
        String idElection2012 = "4f16fe2299c7a10001000012";
        String election2012URL = "http://voxe.org/api/v1/elections/4f16fe2299c7a10001000012";

        Election election = client.resource(election2012URL).get(VoxeResponses.ResponseElection.class).response.election;

        List<Candidate> candidates = new ArrayList<Candidate>();
        for (Candidacy candidacy : election.candidacies) {
            //TODO on considere un candidat par candidature
            Candidate candidate = candidacy.candidates.get(0);
            candidates.add(candidate);
        }

        String searchPropositionsURLBase = "http://voxe.org/api/v1/propositions/search?candidacyIds=";

        Map<Candidate, List<Proposition>> candidatesPropositions = new HashMap<Candidate, List<Proposition>>();
        for (Candidate candidate : candidates) {
            String propositionsURL = searchPropositionsURLBase + candidate.id;
            List<Proposition> propositions = client.resource(propositionsURL).get(VoxeResponses.ResponsePropositions.class).response.propositions;
            candidatesPropositions.put(candidate, propositions);
        }

        return candidatesPropositions;
    }

    private Client buildClient() {
        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(ContextResolverForObjectMapper.class);
        cc.getClasses().add(JacksonJsonProvider.class);
        cc.getFeatures().put("com.sun.jersey.api.json.POJOMappingFeature", true);
        return Client.create(cc);
    }

    @GET
    @Path("tags")
    public Response tags() {
        String urlTags = "http://voxe.org/api/v1/tags/search";
        List<Tag> tags = client.resource(urlTags).get(VoxeResponses.ResponseTags.class).response;
        List<TagJPA> listOfTagJPA = new ArrayList<TagJPA>();        
        for (Tag tag : tags) {
            listOfTagJPA.add(TagJPA.build(tag));
        }
        return Response.ok().build();
    }
}
