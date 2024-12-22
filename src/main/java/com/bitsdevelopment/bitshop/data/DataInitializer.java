package com.bitsdevelopment.bitshop.data;

import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bitsdevelopment.bitshop.model.Role;
import com.bitsdevelopment.bitshop.model.User;
import com.bitsdevelopment.bitshop.repository.UserRepository;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
    Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
    createDefaultRoleIfNotExits(defaultRoles);
    createDefaultAdminIfNotExits();
    createDefaultUserIfNotExits();
  }

  private void createDefaultUserIfNotExits() {
    Role userRole = roleRepository.findByName("ROLE_USER").get();
    for (int i = 01; i <= 3; i++) {
      String defaultEmail = "customer0" + i + "@bitshop.com";
      if (userRepository.existsByEmail(defaultEmail)) {
        continue;
      }
      User user = new User();
      user.setFirstName("Customer");
      user.setLastName("Customer0" + i);
      user.setEmail(defaultEmail);
      user.setPassword(passwordEncoder.encode("customer@bitshop"));
      user.setRoles(Set.of(userRole));
      userRepository.save(user);
      System.out.println("Default user " + i + " created successfully.");
    }
  }

  private void createDefaultAdminIfNotExits() {
    Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
    for (int i = 01; i <= 2; i++) {
      String defaultEmail = "admin0" + i + "@bitshop.com";
      if (userRepository.existsByEmail(defaultEmail)) {
        continue;
      }
      User user = new User();
      user.setFirstName("Admin");
      user.setLastName("Admin0" + i);
      user.setEmail(defaultEmail);
      user.setPassword(passwordEncoder.encode("admin@bitshop"));
      user.setRoles(Set.of(adminRole));
      userRepository.save(user);
      System.out.println("Default admin user " + i + " created successfully.");
    }
  }

  private void createDefaultRoleIfNotExits(Set<String> roles) {
    roles.stream().filter(role -> roleRepository.findByName(role).isEmpty())
        .map(Role::new).forEach(roleRepository::save);
  }

}
