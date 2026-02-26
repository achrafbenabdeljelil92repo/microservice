package org.achraf.ws.stockservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.achraf.ws.stockservice.StockMovementType;

@Data               // generates getters, setters, toString, equals, hashCode
@NoArgsConstructor  // default constructor
@AllArgsConstructor // constructor with all fields
public class StockMovementRequestDto {
    private StockMovementType type;
    private Integer quantity;
    private Long articleId;
    private Long userId;
}
