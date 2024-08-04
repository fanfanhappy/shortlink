package com.nageoffer.shortlink.admin.dto.resp;

import lombok.Data;

/**
 * @projectName: shortlink
 * @package: com.nageoffer.shortlink.admin.dto.resp
 * @className: ShortLinkGroupSaveRespDTO
 * @author: 姬紫衣
 * @description: TODO
 * @date: 2024/8/4 13:53
 * @version: 1.0
 */
@Data
public class ShortLinkGroupSaveRespDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 创建分组用户名
     */
    private String username;

    /**
     * 分组排序
     */
    private Integer sortOrder;
}
