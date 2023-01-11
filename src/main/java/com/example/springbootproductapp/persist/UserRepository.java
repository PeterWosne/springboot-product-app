package com.example.springbootproductapp.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    // если фильтр сложный и запросы варьируются, то желательно использовать Criteria API, если запросы статические то можно и метод попроще

    //способ#3 с Criteria API
    //реализуем интерфейс JpaSpecificationExecutor и обощаем его параметром каким был обощен JpaRepository
    //создаем новый final класс UserSpecification и прописываем статические методы
    //идем в реализацию, прописываем условия

    Optional<User> findUserByUsername(String username);

    //способ#2
//    List<User> findUserByUsernameLike(String username);
//
//    @Query("select u from User u " +
//           "where (u.username like :username or :username is null) and " +
//                 "(u.age >= :minAge or :minAge is null) and " +
//                 "(u.age <= :maxAge or :maxAge is null)")
//    List<User> findWithFilter(@Param("username") String username,
//                              @Param("minAge") Integer minAge,
//                              @Param("maxAge") Integer maxAge);
    //способ с @Query
    //решение нормальное, но запрос сложный, и такой метод может привести к тому, что у БД начнутся проблемы с выполнением запросов;
    //в реальности рекомендуется строить запос таким образом, чтобы он содержал только те условия которые нужны в данном случае
    //то есть например если указан только minAge, то только эта строчка и должна быть в запросе
    //конструируем запрос по частям при помощи Criteria API, пример в директории test




      //Способ#1
      //это более низкоуровневый вариант создания JPAшного репозитория,так сказать по старинке) есть вариант проще
      //по сути этот код всегда одинаковый
//    @PersistenceContext
//    private EntityManager em;
//    //Аннотация @PersistenceContext внедряет прокси,
//    //который выполняет открытие и закрытие EntityManager автоматически.
//
//    public List<User> findAll() {
//        return em.createQuery("from User", User.class)
//                .getResultList();
//    }
//
//    public User findById(int id) {
//        return em.find(User.class, id);
//    }
//
//    public void insert(User user) {
//        em.persist(user);
//    }
//
//    public void update(User user) {
//        em.merge(user);
//    }
//
//    public void delete(int id) {
//        User user = em.find(User.class, id);
//        if(user != null) {
//            em.remove(user);
//        }
//    }
}
