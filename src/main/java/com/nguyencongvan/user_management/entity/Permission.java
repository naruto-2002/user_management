package com.nguyencongvan.user_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Permission {
    @Id
    String name;          // Ví dụ: "READ_PRODUCT_LIST"

    String endpoint;      // Ví dụ: "/api/users"
    String method;        // Ví dụ: "GET"
}
