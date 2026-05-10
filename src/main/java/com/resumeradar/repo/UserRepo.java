package com.resumeradar.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resumeradar.entity.Role;
import com.resumeradar.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>{

	Optional<User> findByEmailAndIsActive(String emailId, boolean b);

	List<User> findByRole(Role role);

	List<User> findByIsActive(boolean b);

	List<User> findByIsActiveAndRole(boolean b, Role role);

	Optional<User> findByEmail(String email);

}
