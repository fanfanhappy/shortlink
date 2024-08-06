package com.nageoffer.shortlink.project.dto.req;

import lombok.Data;

import java.util.Date;

/**
 * 短链接请求创建对象
 */

@Data
public class ShortLinkCreateReqDTO {


    /**
     * 域名
     */
    private String domain;


    /**
     * 原始链接
     */
    private String originUrl;


    /**
     * 分组标识
     */
    private String gid;


    /**
     * 创建类型
     */
    private Integer createdType;

    /**
     * 有效期类型
     */
    private Integer validDateType;

    /**
     * 有效期
     */
    private Date validDate;

    /**
     * 描述
     */
    private String describe;

}
