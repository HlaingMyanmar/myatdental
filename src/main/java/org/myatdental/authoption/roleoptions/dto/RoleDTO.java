package org.myatdental.authoption.roleoptions.dto;

import lombok.Data;
import org.myatdental.authoption.permissionoption.dto.PermissionDTO;

import java.util.List;

@Data
public class RoleDTO {
    private Integer id;
    private String name;
    private String description;
    private List<PermissionDTO> permissions;


}
