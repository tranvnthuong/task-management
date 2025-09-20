package com.tvt.task_mgmt.configuration;

import com.tvt.task_mgmt.entity.Permission;
import com.tvt.task_mgmt.entity.Profile;
import com.tvt.task_mgmt.entity.Role;
import com.tvt.task_mgmt.entity.User;
import com.tvt.task_mgmt.repository.PermissionRepository;
import com.tvt.task_mgmt.repository.ProfileRepository;
import com.tvt.task_mgmt.repository.RoleRepository;
import com.tvt.task_mgmt.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                        RoleRepository roleRepository,
                                        PermissionRepository permissionRepository,
                                        ProfileRepository profileRepository) {
        return args -> {
            Role adminRole = roleRepository.findById("ADMIN").orElse(null);

            if (adminRole == null) {
                Set<Permission> permissionSet = new HashSet<>();
                List<Permission> adminPermissions = List.of(
                        Permission.builder().name("CREATE_DATA").build(),
                        Permission.builder().name("READ_DATA").build(),
                        Permission.builder().name("UPDATE_DATA").build(),
                        Permission.builder().name("DELETE_DATA").build()
                );
                adminPermissions.forEach(permission -> {
                    permissionRepository.save(permission);
                    permissionSet.add(permission);
                });

                adminRole = Role.builder()
                        .name("ADMIN")
                        .permissions(permissionSet)
                        .build();
                roleRepository.save(adminRole);
            }

            if(!roleRepository.existsById("USER")) {
                Set<Permission> permissionSet = new HashSet<>();
                List<Permission> userPermissions = List.of(
                        Permission.builder().name("CREATE_TASK").build(),
                        Permission.builder().name("READ_TASK").build(),
                        Permission.builder().name("UPDATE_TASK").build(),
                        Permission.builder().name("DELETE_TASK").build()
                );
                userPermissions.forEach(permission -> {
                    permissionRepository.save(permission);
                    permissionSet.add(permission);
                });

                roleRepository.save(
                        Role.builder()
                        .name("USER")
                        .permissions(permissionSet)
                        .build()
                );
            }

            if (userRepository.findByUsername("ADMIN").isEmpty()) {

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .email("administrator@gmail.com")
                        .build();

                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                user.setRoles(roles);

                Profile profile = Profile.builder()
                        .avatar("https://image.com")
                        .fullName("Administrator")
                        .dayOfBirth(LocalDate.of(2005, 5, 3))
                        .user(user)
                        .build();

                user.setProfile(profile);
                userRepository.save(user);
                log.warn("admin user has been created width default password: admin, please change it");
            }
        };
    }
}
