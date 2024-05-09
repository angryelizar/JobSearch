package org.example.jobsearch.repositories;

import org.example.jobsearch.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query(value = "update USERS SET AVATAR = :avatar where id = :id", nativeQuery = true)
    void setAvatar(Long id, String avatar);

    @Query(value = "SELECT AVATAR FROM USERS WHERE ID = :id", nativeQuery = true)
    String getAvatarByUserId(Long id);

    List<User> findByName(String username);

    Optional<User> getUserByPhoneNumber(String phoneNumber);

    Boolean existsByPhoneNumber(String phoneNumber);

    Boolean existsByEmail(String email);

    Optional<User> getByEmail(String email);

    @Query(value = "select * from USERS where ACCOUNT_TYPE = 'Соискатель'", nativeQuery = true)
    List<User> getApplicantsUsers();

    @Query(value = "select * from USERS where ACCOUNT_TYPE = 'Работодатель'", nativeQuery = true)
    List<User> getEmployersUsers();
}
