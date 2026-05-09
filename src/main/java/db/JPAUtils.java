package db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtils {
    private static EntityManagerFactory emf;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("default");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    public static void isClose(){
        if(emf.isOpen()) emf.close();
    }
}
