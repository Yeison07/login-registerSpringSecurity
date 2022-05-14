package co.edu.ufps.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.ufps.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
