package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends JpaRepository<Gender, String> {
}
