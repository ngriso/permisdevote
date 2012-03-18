package hte;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import hte.jersey.ContextResolverForObjectMapper;
import hte.jpa.CandidacyJPA;
import hte.jpa.CandidateJPA;
import hte.jpa.JpaUtil;
import hte.jpa.PropositionJPA;
import hte.jpa.QuestionJPA;
import hte.jpa.TagJPA;
import hte.voxe.Candidacy;
import hte.voxe.Candidate;
import hte.voxe.Election;
import hte.voxe.Proposition;
import hte.voxe.Tag;
import hte.voxe.VoxeResponses;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

public class Fetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(Fetcher.class);

    private static final String VOXE_API_BASE = "http://voxe.org/api/v1";
    private static final String ELECTION_2012_URL = VOXE_API_BASE + "/elections/4f16fe2299c7a10001000012";
    private static final String SEARCH_PROPOSITIONS_URL_BASE = VOXE_API_BASE + "/propositions/search?candidacyIds=";
    private static final String URL_TAGS = VOXE_API_BASE + "/tags/search";

    private Client client = buildClient();

    public void runFetch() throws Exception {
        JpaUtil.makeTransactional(new Runnable() {
            @Override
            public void run() {
                LOGGER.debug("[START] fetch tags");
                fetchTags();
                LOGGER.debug("[END] fetch tags");
            }
        });
        JpaUtil.makeTransactional(new Runnable() {
            @Override
            public void run() {
                LOGGER.debug("[START] fetch candidacies");
                fetchCandidates();
                LOGGER.debug("[END] fetch candidacies");
            }
        });
        JpaUtil.makeTransactional(new Runnable() {
            @Override
            public void run() {
                LOGGER.debug("[START] fetch propositions");
                fetchPropositions();
                LOGGER.debug("[END] fetch propositions");
            }
        });

        generateQuestions();
    }

    private void generateQuestions() throws Exception {

        List<CandidacyJPA> candidacyJPAs = JpaUtil.makeTransactional(new Callable<List<CandidacyJPA>>() {
            @Override
            public List<CandidacyJPA> call() throws Exception {
                return JpaUtil.getAllFrom(CandidacyJPA.class);
            }
        });
        final List<PropositionJPA> propositionJPAs = JpaUtil.makeTransactional(new Callable<List<PropositionJPA>>() {
            @Override
            public List<PropositionJPA> call() throws Exception {
                return JpaUtil.getAllFrom(PropositionJPA.class);
            }
        });

        for (final CandidacyJPA candidacyJPA : candidacyJPAs) {
            JpaUtil.makeTransactional(new Runnable() {
                @Override
                public void run() {
                    for (PropositionJPA propositionJPA : propositionJPAs) {
                        QuestionJPA.build(propositionJPA, candidacyJPA);
                    }
                }
            });
        }
    }

    public void fetchCandidates() {
        Election election = client.resource(ELECTION_2012_URL).get(VoxeResponses.ResponseElection.class).response.election;

        for (Candidacy candidacy : election.candidacies) {
            CandidacyJPA candidacyJPA = CandidacyJPA.build(candidacy);
            for (Candidate candidate : candidacy.candidates) {
                CandidateJPA candidateJPA = CandidateJPA.build(candidate);
                candidacyJPA.candidates.add(candidateJPA);
            }
            JpaUtil.save(candidacyJPA);
        }

        for (Election.OrderedTag orderedTag : election.tags) {
            TagJPA tagJPA = JpaUtil.getEntityManager().find(TagJPA.class, orderedTag.id);
            tagJPA.level = 1;
            JpaUtil.update(tagJPA);
        }
    }

    public void fetchPropositions() {
        List<CandidacyJPA> candidacyJPAs = JpaUtil.getAllFrom(CandidacyJPA.class);
        for (final CandidacyJPA candidacyJPA : candidacyJPAs) {
            String propositionsURL = SEARCH_PROPOSITIONS_URL_BASE + candidacyJPA.id;
            final List<Proposition> propositions = client.resource(propositionsURL).get(VoxeResponses.ResponsePropositions.class).response.propositions;
            for (final Proposition proposition : propositions) {
                JpaUtil.makeTransactional(new Runnable() {
                    @Override
                    public void run() {
                        PropositionJPA.build(candidacyJPA, proposition);
                    }
                });
            }
        }
    }

    public void fetchTags() {
        List<Tag> tags = client.resource(URL_TAGS).get(VoxeResponses.ResponseTags.class).response;
        for (Tag tag : tags) {
            TagJPA.build(tag);
        }
    }

    private Client buildClient() {
        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(ContextResolverForObjectMapper.class);
        cc.getClasses().add(JacksonJsonProvider.class);
        cc.getFeatures().put("com.sun.jersey.api.json.POJOMappingFeature", true);
        return Client.create(cc);
    }
}
