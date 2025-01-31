package com.nageoffer.shortlink.admin.dto.req;

import lombok.Data;

/**
 * 分组修改返回实体
 */
@Data
public class ShortLinkGroupUpdateReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名
     */

    private String name;
}
