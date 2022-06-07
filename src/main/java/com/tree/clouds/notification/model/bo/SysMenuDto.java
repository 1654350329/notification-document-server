package com.tree.clouds.notification.model.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class SysMenuDto implements Serializable {

    private String id;
    private String name;
    private String title;
    private String icon;
    private String path;
    private String component;
    private List<SysMenuDto> children = new ArrayList<>();

}
