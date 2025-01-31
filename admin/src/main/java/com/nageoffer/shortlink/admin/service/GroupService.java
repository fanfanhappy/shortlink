package com.nageoffer.shortlink.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nageoffer.shortlink.admin.dao.entity.GroupDO;
import com.nageoffer.shortlink.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.nageoffer.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.ShortLinkGroupSaveRespDTO;

import java.util.List;

/**
 * 短链接分组接口层
 */
public interface GroupService extends IService<GroupDO> {


    /**
     * 新增分组名
     * @param groupName
     */
    void saveGroup(String groupName);

    /**
     * 查询短链接分组集合
     * @return
     */
    List<ShortLinkGroupSaveRespDTO> listGroup();


    /**
     * 修改短链接分组名
     * @param updateReqDTO
     */
    void updateGroup(ShortLinkGroupUpdateReqDTO updateReqDTO);

    /**
     * 删除短链接分组
     * @param gid
     */
    void deleteGroup(String gid);


    /**
     * 短链接分组排序
     * @param sortReqDTO
     */
    void sortGroup(List<ShortLinkGroupSortReqDTO> sortReqDTO);
}
