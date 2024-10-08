package com.example.shoppingmall.domain.user.dao;

import com.example.shoppingmall.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);


    @Query("select u from User u join fetch u.cart where u.id = :userId")
    Optional<User> findUserWithCartByUserId(@Param("userId") Long userId);
}
