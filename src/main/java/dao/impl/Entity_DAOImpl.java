package dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import config.RestaurantApplication;
import dao.Entity_DAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class Entity_DAOImpl<E> implements Entity_DAO<E>{
	protected final RestaurantApplication restaurantApplication = RestaurantApplication.getInstance();
    protected final EntityManagerFactory entityManagerFactory = restaurantApplication.getEntityManagerFactory();

    @Override
    public boolean themNhieu(List<E> listEntity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {
            entityTransaction.begin();

            for (E entity : listEntity) {
                entityManager.persist(entity);
            }

            entityTransaction.commit();
            return true; // Trả về true nếu lưu thành công toàn bộ danh sách

        } catch (Exception e) {
            e.printStackTrace();
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            return false;
        } finally {
            entityManager.close();
        }
    }

    public boolean them(E entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        if (entity != null) {
            try {
                entityTransaction.begin();
                entityManager.persist(entity);
                entityTransaction.commit();
                return true;
            } catch (Exception e) {
                e.printStackTrace(); // Ghi lại thông báo lỗi
                if (entityTransaction.isActive()) {
                    entityTransaction.rollback(); // Chỉ rollback nếu giao dịch đang hoạt động
                }
            } finally {
                entityManager.close(); // Đảm bảo đóng entityManager
            }
        }
        return false; // Trả về false nếu entity là null hoặc nếu có lỗi
    }

    @Override
    public boolean capNhat(E entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        if (entity != null) {
            try {
                entityTransaction.begin();
                entityManager.merge(entity);
                entityTransaction.commit();
                return true;
            } catch (Exception e) {
                entityTransaction.rollback();
            }
        }
        entityManager.close();
        return false;
    }

    @Override
    public List<E> getDanhSach(String namedQuery, Class<E> entityType) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager
                    .createNamedQuery(namedQuery, entityType)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        entityManager.close();
        return null;
    }

    @Override
    public List<E> getDanhSach(String namedQuery, Class<E> entityType, int limit, int skip) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<E> typedQuery = entityManager.createNamedQuery(namedQuery, entityType);
            // Set Limit value
            typedQuery.setMaxResults(limit);
            // Set Skip value
            typedQuery.setFirstResult(skip);
            return typedQuery.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        entityManager.close();
        return null;
    }

    @Override
    public List<E> getDanhSach(Class<E> entityType, Map<String, Object> parameters, int limit, int skip) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<E> criteriaBuilderQuery =  criteriaBuilder.createQuery(entityType);
            Root<E> root = criteriaBuilderQuery.from(entityType);
            // Danh sách các điều kiện where
            List<Predicate> predicates = new ArrayList<>();
            // Thêm điều kiện where cho mỗi mục trong map
            if (parameters != null) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    String field = entry.getKey();
                    Object value = entry.getValue();

                    // Chỉ thêm điều kiện nếu giá trị không null
                    if (value != null) {
                        predicates.add(criteriaBuilder.equal(root.get(field), value));
                    }
                }
            }
            // Áp dụng các điều kiện where (nếu có)
            criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

            // Thực thi truy vấn và lấy kết quả
            return entityManager.createQuery(criteriaBuilderQuery)
                    .setMaxResults(limit)
                    .setFirstResult(skip)
                    .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }

    public List<E> getDanhSach(Class<E> entityType, Map<String, Object> parameters) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<E> criteriaBuilderQuery =  criteriaBuilder.createQuery(entityType);
            Root<E> root = criteriaBuilderQuery.from(entityType);
            // Danh sách các điều kiện where
            List<Predicate> predicates = new ArrayList<>();

            // Thêm điều kiện where cho mỗi mục trong map
            if (parameters != null) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    String field = entry.getKey();
                    Object value = entry.getValue();

                    // Chỉ thêm điều kiện nếu giá trị không null
                    if (value != null) {
                        predicates.add(criteriaBuilder.equal(root.get(field), value));
                    }
                }
            }
            // Áp dụng các điều kiện where (nếu có)
            criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));
            // Thực thi truy vấn và lấy kết quả
            return entityManager.createQuery(criteriaBuilderQuery).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }
    
    @Override
    public List<E> getDanhSachByDate(int day, int month, int year, Class<E> entityType, String nameColumn) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<E> query = criteriaBuilder.createQuery(entityType);
            Root<E> root = query.from(entityType);
            // Danh sách các điều kiện
            List<Predicate> predicates = new ArrayList<>();
            // Thêm điều kiện kiểm tra ngày
            if (day != -1) {
                System.out.println(day);
                predicates.add(criteriaBuilder.equal(criteriaBuilder.function("day", Integer.class, root.get(nameColumn)), day));
            }
            // Thêm điều kiện kiểm tra tháng
            if (month != -1) {
                System.out.println(month);
                predicates.add(criteriaBuilder.equal(criteriaBuilder.function("month", Integer.class, root.get(nameColumn)), month));
            }
            // Thêm điều kiện kiểm tra năm
            if (year != -1) {
                System.out.println(year);
                predicates.add(criteriaBuilder.equal(criteriaBuilder.function("year", Integer.class, root.get(nameColumn)), year));
            }
            // Áp dụng các điều kiện vào truy vấn
            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
            // Thực thi truy vấn và trả về danh sách kết quả
            return entityManager.createQuery(query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }
    
    @Override
    public Long count(String namedQuery, Class<E> entityType) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return (Long) entityManager
                    .createNamedQuery(namedQuery, entityType)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        entityManager.close();
        return null;
    }
    
    @Override
    public boolean xoa(E entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        if (entity != null) {
            try {
                entityTransaction.begin();

                // 1. Lấy ID của đối tượng
                Object entityId = entityManagerFactory.getPersistenceUnitUtil().getIdentifier(entity);
                
                // LẤY LOẠI CLASS CHÍNH XÁC (Sử dụng casting an toàn)
                // Lấy kiểu Class của đối tượng E
                @SuppressWarnings("unchecked")
                Class<E> entityClass = (Class<E>) entity.getClass();
                
                // TÌM LẠI ĐỐI TƯỢNG VỚI CLASS VÀ ID ĐÃ CÓ
                E managedEntity = entityManager.find(entityClass, entityId);

                if (managedEntity != null) {
                    entityManager.remove(managedEntity); // Thực hiện xóa
                    entityTransaction.commit();
                    return true;
                } else {
                    entityTransaction.rollback(); 
                    return false; 
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (entityTransaction.isActive()) {
                    entityTransaction.rollback();
                }
            } finally {
                entityManager.close();
            }
        }
        return false;
    }

}
