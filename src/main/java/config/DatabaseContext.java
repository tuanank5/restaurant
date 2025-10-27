package config;

import dao.Entity_DAO;

public interface DatabaseContext {
	<R extends Entity_DAO<?>> R newEntity_DAO(Class<R> entityType);
}
