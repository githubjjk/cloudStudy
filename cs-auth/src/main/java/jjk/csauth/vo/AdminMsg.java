package jjk.csauth.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
public class AdminMsg {
    /**
     * 姓名
     */
    private String name;

    /**
     * 用户头像路径
     */
    private String avatar;

    /**
     * 角色代号列表
     */
    private Set<String> roles = new HashSet<>();

    /**
     * 用户介绍
     */
    private String introduction;
}
