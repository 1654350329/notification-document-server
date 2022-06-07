package com.tree.clouds.notification.security;


import com.tree.clouds.notification.model.entity.User;
import com.tree.clouds.notification.service.UserService;
import com.tree.clouds.notification.utils.BaseBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userManage = userService.getUserByAccount(username);
        if (userManage == null) {
            throw new BaseBusinessException(400, "账号不存在");
        }
        if (userManage.getStatus() == 0) {
            throw new BaseBusinessException(400, "账号已停用");
        }
        return new AccountUser(userManage.getUserId(), userManage.getAccount(), userManage.getPassword(), getUserAuthority(userManage.getUserId()), userManage.getRegionId());
    }

    /**
     * 获取用户权限信息（角色、菜单权限）
     *
     * @param userId
     * @return
     */
    public List<GrantedAuthority> getUserAuthority(String userId) {

        // 角色(ROLE_admin)、菜单操作权限 sys:user:list
        String authority = userService.getUserAuthorityInfo(userId);  // ROLE_admin,ROLE_normal,sys:user:list,....
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
