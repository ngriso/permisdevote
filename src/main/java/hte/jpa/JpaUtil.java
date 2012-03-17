package hte.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

public class JpaUtil {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("xskills");

    private static final ThreadLocal<EntityManager> entityManagerHolder = new ThreadLocal<EntityManager>();

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

    public static EntityManager getEntityManager() {
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
}
