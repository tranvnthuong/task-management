package com.tvt.task_mgmt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Profile {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String avatar;
    String fullName;
    LocalDate dayOfBirth;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    @UpdateTimestamp
    LocalDateTime updatedAt;
}
