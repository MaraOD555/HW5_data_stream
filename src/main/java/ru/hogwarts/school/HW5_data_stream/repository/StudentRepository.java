package ru.hogwarts.school.HW5_data_stream.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.HW5_data_stream.model.Faculty;
import ru.hogwarts.school.HW5_data_stream.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> { //Spring ищет сущности по определенным полям
    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int from, int to);

   Faculty findFaculty(long id);
}
