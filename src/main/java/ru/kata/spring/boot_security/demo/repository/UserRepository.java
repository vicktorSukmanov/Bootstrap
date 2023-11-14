package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kata.spring.boot_security.demo.model.User;



public interface UserRepository extends JpaRepository<User, Long> {
    @Query("from User u join fetch u.roles where u.username = :username")
    User findByUsername(@Param("username") String username);


}
