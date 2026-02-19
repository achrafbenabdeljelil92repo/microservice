package org.achraf.ws.stockservice.repository;

import org.achraf.ws.stockservice.entities.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
}
