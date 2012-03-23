package p2v.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class JpaUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JpaUtil.class);

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("permisdevote");

    private static final ThreadLocal<EntityManager> entityManagerHolder = new ThreadLocal<EntityManager>();

    private static final ThreadLocal<Map<Class, List>> cache = new ThreadLocal<Map<Class, List>>();

    public static void init() {

    }

    private static EntityManager createEntityManager() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManagerHolder.set(entityManager);
        return entityManager;
    }

    private static void releaseEntityManager() {
        entityManagerHolder.set(null);
    }

    private static EntityManager getEntityManager() {
        return entityManagerHolder.get();
    }

    public static void makeTransactional(Runnable template) {
        EntityManager entityManager = createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            template.run();
            transaction.commit();
        } catch (PersistenceException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
            releaseEntityManager();
        }
    }

    public static <T> T makeTransactional(Callable<T> template) throws Exception {
        EntityManager entityManager = createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            T result = template.call();
            transaction.commit();
            return result;
        } catch (PersistenceException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
            releaseEntityManager();
        }
    }

    public static <T> List<T> getAllFrom(Class<T> clazz) {
        Map<Class, List> resultLists = cache.get();
        if (resultLists == null) {
            resultLists = new HashMap<Class, List>();
            cache.set(resultLists);
        }
        List all = resultLists.get(clazz);
        if (all != null) {
            return all;
        }

        EntityManager entityManager = JpaUtil.getEntityManager();
        CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(clazz);
        Root<T> root = query.from(clazz);
        List<T> resultList = entityManager.createQuery(query.select(root)).getResultList();
        resultLists.put(clazz, resultList);
        return resultList;
    }

    public static void save(Object entity) {
        JpaUtil.getEntityManager().persist(entity);
        LOGGER.info("[Created] " + entity);
    }

    public static <T> T update(T entity) {
        T merge = JpaUtil.getEntityManager().merge(entity);
        LOGGER.info("[Merged] " + merge);
        return merge;
    }

    public static List<TagJPA> findThemes() {
        return getEntityManager().createQuery("from TagJPA where level = 1 order by name", TagJPA.class).getResultList();
    }

    public static <T> T findById(Class<T> clazz, Long id) {
        EntityManager entityManager = getEntityManager();
        T entity = entityManager.find(clazz, id);
        if (null == entity) {
            throw new NoResultException("id: " + id + ", Entity class: " + clazz.getName());
        }
        return entity;
    }

    public static ResponseJPA findResponseByVoterAndQuestion(Long questionId, VoterJPA voter) {
        return getEntityManager()
                .createQuery("from ResponseJPA where voter = :electeur and question.id = :questionId", ResponseJPA.class)
                .setParameter("electeur", voter)
                .setParameter("questionId", questionId)
                .getSingleResult();
    }

    public static List<Long> findQuestionsIdByCandidacyId(String candidacyId) {
        return getEntityManager()
                .createQuery("select id from QuestionJPA where candidacy.id = :candidacyId", Long.class)
                .setParameter("candidacyId", candidacyId)
                .getResultList();
    }

    public static List<Long> findQuestionsIdByTagId(String tagId) {
        return getEntityManager()
                .createQuery("select id from QuestionJPA where tagLevel1.id = :tagId", Long.class)
                .setParameter("tagId", tagId)
                .getResultList();
    }

    public static UserStatsJPA findUserStatsByVoter(VoterJPA voter) {
        return getEntityManager()
                .createQuery("from UserStatsJPA where voter = :electeur", UserStatsJPA.class)
                .setParameter("electeur", voter)
                .getSingleResult();
    }

    public static VoterJPA findVoterByUserID(String userID) {
        return getEntityManager()
                .createQuery("from VoterJPA where id = :userID", VoterJPA.class)
                .setParameter("userID", userID)
                .getSingleResult();
    }

    public static List<PropositionJPA> findPropositionsByCandidacy(CandidacyJPA candidacy) {
        return getEntityManager()
                .createQuery("from PropositionJPA where candidacy = :candidacy", PropositionJPA.class)
                .setParameter("candidacy", candidacy)
                .getResultList();
    }

    public static List<PropositionJPA> findAllPropositionsExceptForOneCandidacy(CandidacyJPA candidacy) {
        return getEntityManager()
                .createQuery("from PropositionJPA where candidacy != :candidacy", PropositionJPA.class)
                .setParameter("candidacy", candidacy)
                .getResultList();
    }
}
