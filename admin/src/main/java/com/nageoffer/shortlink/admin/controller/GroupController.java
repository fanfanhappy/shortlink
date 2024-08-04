package com.nageoffer.shortlink.admin.controller;

import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.common.convention.result.Results;
import com.nageoffer.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.nageoffer.shortlink.admin.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接分组控制层
 */
@RestController
@AllArgsConstructor
public class GroupController {

    private final GroupService groupService;


    @PostMapping("/api/short-link/admin/v1/group")
    private Result<Void> creatGroup(@RequestBody ShortLinkGroupSaveReqDTO groupSaveReqDTO)
    {
        groupService.saveGroup(groupSaveReqDTO.getName());
        return Results.success();
    }
}
