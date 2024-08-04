package com.nageoffer.shortlink.admin.controller;

import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.common.convention.result.Results;
import com.nageoffer.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.nageoffer.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.ShortLinkGroupSaveRespDTO;
import com.nageoffer.shortlink.admin.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接分组控制层
 */
@RestController
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;


    /**
     * 添加短链接分组名称
     * @param groupSaveReqDTO
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/group")
    private Result<Void> creatGroup(@RequestBody ShortLinkGroupSaveReqDTO groupSaveReqDTO)
    {
        groupService.saveGroup(groupSaveReqDTO.getName());
        return Results.success();
    }

    /**
     * 查询分组集合信息
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/group")
    public Result<List<ShortLinkGroupSaveRespDTO>> ListGroup()
    {
        return Results.success(groupService.listGroup());
    }

    /**
     * 修改短链接分组名
     * @param updateReqDTO
     * @return
     */
    @PutMapping("/api/short-link/admin/v1/group")
    public Result<Void> updateGroup(@RequestBody ShortLinkGroupUpdateReqDTO updateReqDTO)
    {
        groupService.updateGroup(updateReqDTO);
        return Results.success();
    }
}
