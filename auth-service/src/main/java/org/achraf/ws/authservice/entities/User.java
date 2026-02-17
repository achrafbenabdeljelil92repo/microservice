package org.achraf.ws.authservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.achraf.ws.authservice.enums.LoginType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="app_user")
@Getter @Setter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;
  @Column(unique=true)
  private String username;

  @Enumerated(EnumType.STRING)
  private LoginType loginType;
  private String email;

  @ManyToMany(
          fetch = FetchType.EAGER,
          cascade = CascadeType.PERSIST
  )
  @JoinTable(
          name="user_role",
          joinColumns = @JoinColumn(name="user_id"),
          inverseJoinColumns = @JoinColumn(name="role_id")
  )
  private Set<Role> roles = new HashSet<>();
  @OneToMany(cascade = {CascadeType.REMOVE},
          orphanRemoval = true,mappedBy = "user",
          fetch = FetchType.LAZY)
  private Set<Activity> activities = new HashSet<>();

  public void addActivity(Activity activity) {
    activities.add(activity);
    activity.setUser(this); // important!
  }

  public void removeActivity(Activity activity) {
    activities.remove(activity);
  }
}
