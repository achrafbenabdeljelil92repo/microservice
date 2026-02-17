package org.achraf.ws.authservice.repositories;

import org.achraf.ws.authservice.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityAuthRepository extends JpaRepository<Activity, Long> {

}
