package org.example.jobsearch.repositories;

import org.example.jobsearch.models.Vacancy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    @Query(value = "select * from vacancies where category_id = :categoryId", nativeQuery = true)
    List<Vacancy> getVacanciesByCategoryId(Long categoryId);

    @Query(value = "select * from VACANCIES where UPPER(NAME) like UPPER(:query) or upper(DESCRIPTION) like UPPER(:query) and IS_ACTIVE = TRUE", nativeQuery = true)
    List<Vacancy> getVacanciesByQuery(String query);

    @Query(value = "select * from VACANCIES where AUTHOR_ID = :authorId and IS_ACTIVE = true", nativeQuery = true)
    List<Vacancy> getVacanciesByAuthorId(Long authorId);

    @Query(value = "SELECT COUNT(*) FROM VACANCIES WHERE  AUTHOR_ID = :authorId AND IS_ACTIVE = TRUE", nativeQuery = true)
    Integer getCountVacanciesByAuthorId(Long authorId);

    List<Vacancy> searchVacanciesByIsActiveEquals(Boolean isActive);
    List<Vacancy> findVacanciesByAuthorId(Long authorId);
    List<Vacancy> findVacanciesByAuthorIdAndIsActiveEquals(Long authorId, Boolean isActive);
}
