package org.achraf.ws.stockservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.achraf.ws.stockservice.StockMovementType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementResponseDto {

    private Long id;
    private StockMovementType type;
    private Integer quantity;
    private LocalDateTime movementDate;
    private Long articleId;
    private UserDTO user;
}