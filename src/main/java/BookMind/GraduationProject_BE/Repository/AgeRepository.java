package BookMind.GraduationProject_BE.Repository;

import BookMind.GraduationProject_BE.Entity.Age;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgeRepository extends JpaRepository<Age, Integer> {
}
