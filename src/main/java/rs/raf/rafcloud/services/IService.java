package rs.raf.rafcloud.services;


public interface IService<T, ID> {
    <S extends T> S save(S var1);

    T findById(ID var1);

    void deleteById(ID var1);
}
