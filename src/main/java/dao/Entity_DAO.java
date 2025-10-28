package dao;

import java.util.List;
import java.util.Map;

public interface Entity_DAO<E> {
	
	boolean them(E entity);
    boolean themNhieu(List<E> listEntity);
    boolean capNhat(E entity);
    boolean xoa(E entity); 

    List<E> getDanhSach(String namedQuery, Class<E> entityType);
    List<E> getDanhSach(String namedQuery, Class<E> entityType, int limit, int skip);
    List<E> getDanhSach(Class<E> entityType, Map<String, Object> parameters);
    List<E> getDanhSach(Class<E> entityType, Map<String, Object> parameters, int limit, int skip);
    List<E> getDanhSachByDate(int day, int month, int year, Class<E> entityType, String nameColumn);
    Long count(String namedQuery, Class<E> entityType);
    
}