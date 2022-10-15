package ru.hogwarts.school.HW5_data_stream.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.HW5_data_stream.model.Avatar;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
     Optional<Avatar> findAvatarByStudentId (long studentId);
}
