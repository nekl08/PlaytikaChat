package ua.nekl08.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.nekl08.demo.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
}