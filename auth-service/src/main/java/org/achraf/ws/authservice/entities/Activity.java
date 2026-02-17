package org.achraf.ws.authservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.achraf.ws.authservice.enums.ActivityType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    ActivityType  activityType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true, foreignKey = @ForeignKey(name = "FK_ACTIVITY_USER"))
    private User user;

    @Column(updatable = false)
    LocalDateTime date;


}
