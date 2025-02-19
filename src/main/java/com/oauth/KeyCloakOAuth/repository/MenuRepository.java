package com.oauth.KeyCloakOAuth.repository;

import com.oauth.KeyCloakOAuth.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {}