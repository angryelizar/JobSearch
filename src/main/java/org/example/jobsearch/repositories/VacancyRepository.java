package org.example.jobsearch.repositories;

import org.example.jobsearch.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    @Query(value = "select * from vacancies where category_id = :categoryId", nativeQuery = true)
    List<Vacancy> getVacanciesByCategoryId(Long categoryId);

    @Query(value = "select * from VACANCIES where NAME like :query or DESCRIPTION like :query", nativeQuery = true)
    List<Vacancy> getVacanciesByQuery(String query);

    @Query(value = "select * from VACANCIES where AUTHOR_ID = :authorId and IS_ACTIVE = true", nativeQuery = true)
    List<Vacancy> getVacanciesByAuthorId(Long authorId);

    @Query(value = "SELECT COUNT(*) FROM VACANCIES WHERE  AUTHOR_ID = :authorId", nativeQuery = true)
    Integer getCountVacanciesByAuthorId(Long authorId);

    List<Vacancy> searchVacanciesByIsActiveEquals(Boolean isActive);
    List<Vacancy> findVacanciesByAuthorId(Long authorId);
}
