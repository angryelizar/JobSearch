package org.example.jobsearch.repositories;

import org.example.jobsearch.models.RespondApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RespondedApplicantRepository extends JpaRepository<RespondApplicant, Long> {
    @Query(value = """
            select * from RESPONDED_APPLICANTS ra
                            WHERE ra.RESUME_ID in (
                                SELECT ID
                                from RESUMES
                                where APPLICANT_ID = (
                                    select id
                                    from users
                                    where EMAIL = :email
                                    )
                                )""", nativeQuery = true)
    List<RespondApplicant> findAllByApplicantEmail(String email);

    @Query(value = """
            SELECT COUNT(*) FROM RESPONDED_APPLICANTS rs
                WHERE RESUME_ID in (
                SELECT ID 
                FROM RESUMES
                WHERE APPLICANT_ID = :userId) and rs.CONFIRMATION = true;
                 """, nativeQuery = true)
    Integer getApprovedResponsesNumber(Long userId);

    @Query(value = """
            select * from RESPONDED_APPLICANTS ra
                            WHERE VACANCY_ID in (
                                SELECT ID
                                from VACANCIES
                                where AUTHOR_ID = (
                                    select id
                                    from users
                                    where EMAIL = :email
                                    )
                                )
            """, nativeQuery = true)
    List<RespondApplicant> getByEmployerEmail(String email);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE RESPONDED_APPLICANTS
                            SET CONFIRMATION = false
                            WHERE RESUME_ID = :resume AND VACANCY_ID = :vacancy""", nativeQuery = true)
    void denyResponse(Long resume, Long vacancy);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE RESPONDED_APPLICANTS
                            SET CONFIRMATION = true
                            WHERE RESUME_ID = :resume AND VACANCY_ID = :vacancy""", nativeQuery = true)
    void acceptResponse(Long resume, Long vacancy);

    @Query(value = """
            SELECT * FROM RESPONDED_APPLICANTS
                            WHERE ID = :id""", nativeQuery = true)
    RespondApplicant getById(Long id);

    @Query(value = "select * from RESPONDED_APPLICANTS  where VACANCY_ID = :id", nativeQuery = true)
    List<RespondApplicant> getRespondedApplicantsByVacancyId(Long id);

    @Query(value = "SELECT COUNT(*) FROM RESPONDED_APPLICANTS WHERE RESUME_ID = :resumeId and VACANCY_ID = :vacancyId", nativeQuery = true)
    Integer isExists(Long resumeId, Long vacancyId);
}
