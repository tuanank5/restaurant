package config;

import config.impl.DatabaseContextImpl;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class RestaurantApplication {
	private final EntityManagerFactory entityManagerFactory;
    private final DatabaseContext databaseContext;
    private static final RestaurantApplication instance = new RestaurantApplication();
    
    public RestaurantApplication() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("default");
        this.databaseContext = new DatabaseContextImpl();
    }
    public static RestaurantApplication getInstance() {
        return instance;
    }

    public DatabaseContext getDatabaseContext() {
        return databaseContext;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
