package org.achraf.ws.authservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data               // generates getters, setters, toString, equals, hashCode
@NoArgsConstructor  // default constructor
@AllArgsConstructor // constructor with all fields
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private Set<String> roles; // role names only
}
