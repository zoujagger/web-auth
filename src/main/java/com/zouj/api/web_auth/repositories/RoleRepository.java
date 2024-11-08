package com.zouj.api.web_auth.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.zouj.api.web_auth.entities.Role;
import com.zouj.api.web_auth.entities.RoleEnum;

public interface RoleRepository extends CrudRepository<Role, Integer>{
    Optional<Role> findByName(RoleEnum name);
}
