package p2v;

import com.google.common.collect.Iterables;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import p2v.jpa.CandidacyJPA;
import p2v.jpa.CandidateJPA;
import p2v.jpa.JpaUtil;
import p2v.jpa.PropositionJPA;
import p2v.jpa.QuestionJPA;
import p2v.jpa.TagJPA;
import p2v.voxe.Candidacy;
import p2v.voxe.Candidate;
import p2v.voxe.Election;
import p2v.voxe.Proposition;
import p2v.voxe.Tag;
import p2v.voxe.VoxeResponses;
import p2v.web.jaxrs.ContextResolverForObjectMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class Fetcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(Fetcher.class);

    private static final String VOXE_API_BASE = "http://voxe.org/api/v1";
    private static final String ELECTION_2012_URL = VOXE_API_BASE + "/elections/4f16fe2299c7a10001000012";
    private static final String SEARCH_PROPOSITIONS_URL_BASE = VOXE_API_BASE + "/propositions/search?candidacyIds=";
    private static final String URL_TAGS = VOXE_API_BASE + "/tags/search";

    private Client client = buildClient();

    public void runFetch() throws Exception {
        fetchTags();
        fetchCandidates();
        fetchPropositions();
        generateQuestions();
    }

    private void generateQuestions() throws Exception {
        LOGGER.debug("[START] generating questions");
        List<CandidacyJPA> candidacyJPAs = JpaUtil.makeTransactional(new Callable<List<CandidacyJPA>>() {
            @Override
            public List<CandidacyJPA> call() throws Exception {
                return JpaUtil.getAllFrom(CandidacyJPA.class);
            }
        });
        for (final CandidacyJPA candidacyJPA : candidacyJPAs) {
            generateQuestionsForOneCandidacy(candidacyJPA);
        }
        LOGGER.debug("[END] generating questions");
    }

    public void generateQuestionsForOneCandidacy(final CandidacyJPA candidacy) throws Exception {
        final List<PropositionJPA> myPropositions = JpaUtil.makeTransactional(new Callable<List<PropositionJPA>>() {
            @Override
            public List<PropositionJPA> call() throws Exception {
                return JpaUtil.findPropositionsByCandidacy(candidacy);
            }
        });
        JpaUtil.makeTransactional(new Runnable() {
            @Override
            public void run() {
                CandidacyJPA candidacyJPA = JpaUtil.update(candidacy);
                for (PropositionJPA propositionJPA : myPropositions) {
                    QuestionJPA.build(propositionJPA, candidacyJPA);
                }
            }
        });
        final List<PropositionJPA> othersPropositions = JpaUtil.makeTransactional(new Callable<List<PropositionJPA>>() {
            @Override
            public List<PropositionJPA> call() throws Exception {
                return JpaUtil.findAllPropositionsExceptForOneCandidacy(candidacy);
            }
        });

        Collections.shuffle(othersPropositions);
        JpaUtil.makeTransactional(new Runnable() {
            @Override
            public void run() {
                CandidacyJPA candidacyJPA = JpaUtil.update(candidacy);
                for (PropositionJPA propositionJPA : Iterables.limit(othersPropositions, myPropositions.size())) {
                    QuestionJPA.build(propositionJPA, candidacyJPA);
                }
            }
        });
        LOGGER.info("{} generated questions for candidacy {}", myPropositions.size() * 2, candidacy);
    }

    public void fetchCandidates() throws Exception {
        LOGGER.debug("[START] fetch candidacies");
        final Election election = client.resource(ELECTION_2012_URL).get(VoxeResponses.ResponseElection.class).response.election;

        JpaUtil.makeTransactional(new Runnable() {
            @Override
            public void run() {
                for (Candidacy candidacy : election.candidacies) {
                    CandidacyJPA candidacyJPA = CandidacyJPA.build(candidacy);
                    for (Candidate candidate : candidacy.candidates) {
                        CandidateJPA.build(candidate, candidacyJPA);
                    }
                    JpaUtil.save(candidacyJPA);
                }
                LOGGER.info("Collected candidacies: " + election.candidacies.size());
            }
        });

        final List<TagJPA> tagJPAs = JpaUtil.makeTransactional(new Callable<List<TagJPA>>() {
            @Override
            public List<TagJPA> call() throws Exception {
                return JpaUtil.getAllFrom(TagJPA.class);
            }
        });
        final Map<String, TagJPA> tagsMap = new HashMap<String, TagJPA>();
        for (TagJPA tag : tagJPAs) {
            tagsMap.put(tag.id, tag);
        }

        JpaUtil.makeTransactional(new Runnable() {
            @Override
            public void run() {
                for (Election.OrderedTag orderedTag : election.tags) {
                    TagJPA tagJPA = tagsMap.get(orderedTag.id);
                    tagJPA.level = 1;
                    JpaUtil.update(tagJPA);
                }
            }
        });
        LOGGER.debug("[END] fetch candidacies");
    }

    public void fetchPropositions() throws Exception {
        final List<CandidacyJPA> candidacyJPAs = JpaUtil.makeTransactional(new Callable<List<CandidacyJPA>>() {
            @Override
            public List<CandidacyJPA> call() throws Exception {
                return JpaUtil.getAllFrom(CandidacyJPA.class);
            }
        });
        final List<TagJPA> tagJPAs = JpaUtil.makeTransactional(new Callable<List<TagJPA>>() {
            @Override
            public List<TagJPA> call() throws Exception {
                return JpaUtil.getAllFrom(TagJPA.class);
            }
        });
        final Map<String, TagJPA> tagsMap = new HashMap<String, TagJPA>();
        for (TagJPA tag : tagJPAs) {
            tagsMap.put(tag.id, tag);
        }

        LOGGER.debug("[START] collect propositions");
        for (final CandidacyJPA candidacyJPA : candidacyJPAs) {
            String propositionsURL = SEARCH_PROPOSITIONS_URL_BASE + candidacyJPA.id;
            final List<Proposition> propositions = client.resource(propositionsURL).get(VoxeResponses.ResponsePropositions.class).response.propositions;
            for (final Proposition proposition : propositions) {
                JpaUtil.makeTransactional(new Runnable() {
                    @Override
                    public void run() {
                        PropositionJPA.build(candidacyJPA, proposition, tagsMap);
                    }
                });
            }
            LOGGER.info("{} collected propositions for candidacy {}", propositions.size(), candidacyJPA);
        }
        LOGGER.debug("[END] collect propositions");
    }

    public void fetchTags() {
        final List<Tag> tags = client.resource(URL_TAGS).get(VoxeResponses.ResponseTags.class).response;
        JpaUtil.makeTransactional(new Runnable() {
            @Override
            public void run() {
                LOGGER.debug("[START] collect tags");
                for (Tag tag : tags) {
                    TagJPA.build(tag);
                }
                LOGGER.debug("[END] collect tags");
                int size = JpaUtil.getAllFrom(TagJPA.class).size();
                LOGGER.info("Collected tags: " + size);
            }
        });
    }

    private Client buildClient() {
        System.getProperties().put("http.proxyHost", "gecd-proxy");
        System.getProperties().put("http.proxyPort", "8080");

        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(ContextResolverForObjectMapper.class);
        cc.getClasses().add(JacksonJsonProvider.class);
        cc.getFeatures().put("com.sun.jersey.api.json.POJOMappingFeature", true);
        return Client.create(cc);
    }
}
