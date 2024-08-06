package com.nageoffer.shortlink.admin.dto.req;


import lombok.Data;

@Data
public class ShortLinkGroupSortReqDTO {

    /**
     * 分组id
     */
    private String gid;

    /**
     * 排序字段
     */

    private Integer sortOrder;
}
