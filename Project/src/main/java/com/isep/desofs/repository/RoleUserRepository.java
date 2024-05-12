package com.isep.desofs.repository;

import com.isep.desofs.domain.RoleUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RoleUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {}
