package com.shopsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.shopsphere.utils.AppRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private AppRole roleName;
}
