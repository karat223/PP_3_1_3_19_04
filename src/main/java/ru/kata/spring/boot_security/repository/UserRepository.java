package ru.kata.spring.boot_security.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"roles"})
    User findByEmail(String email);

    @EntityGraph(attributePaths = {"roles"})
    @Override
    Optional<User> findById(Long id);

    @EntityGraph(attributePaths = {"roles"})
    @Query("SELECT u FROM User u")
    List<User> findAllWithRoles();
}
