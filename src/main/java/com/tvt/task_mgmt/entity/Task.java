package com.tvt.task_mgmt.entity;

import com.tvt.task_mgmt.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String title;
    String description;

    @Enumerated(EnumType.STRING)
    TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User assignedTo;

    LocalDateTime dueDate;

    @CreationTimestamp
    LocalDateTime createdAt;
}
