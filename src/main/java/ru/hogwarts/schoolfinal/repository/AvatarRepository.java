package ru.hogwarts.schoolfinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.schoolfinal.entity.Avatar;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByStudent_Id(long studentId);

}
