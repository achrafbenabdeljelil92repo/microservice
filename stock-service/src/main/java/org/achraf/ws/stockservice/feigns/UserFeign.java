package org.achraf.ws.stockservice.feigns;

import org.achraf.ws.stockservice.dtos.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("auth-service")
public interface  UserFeign {
    @GetMapping("/{username}")
    ResponseEntity<UserDTO> getUserByUsername(@PathVariable("username") String username);
}
