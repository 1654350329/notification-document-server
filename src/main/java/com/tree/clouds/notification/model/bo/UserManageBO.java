package com.tree.clouds.notification.model.bo;

import com.tree.clouds.notification.model.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserManageBO extends User {
    @ApiModelProperty("角色id")
    @NotNull(message = "至少绑定一种角色")
    private List<String> roleIds;

}
