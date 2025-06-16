package com.nguyencongvan.user_management.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionRequest {
    String name;          // Ví dụ: "USER_READ"
    String endpoint;      // Ví dụ: "/api/users"
    String method;        // Ví dụ: "GET", "POST", "PUT", "DELETE"
}
