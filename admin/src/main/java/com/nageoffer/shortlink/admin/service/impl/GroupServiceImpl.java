package com.nageoffer.shortlink.admin.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nageoffer.shortlink.admin.common.biz.user.UserContext;
import com.nageoffer.shortlink.admin.common.database.BaseDO;
import com.nageoffer.shortlink.admin.dao.entity.GroupDO;
import com.nageoffer.shortlink.admin.dao.mapper.GroupMapper;
import com.nageoffer.shortlink.admin.dto.resp.ShortLinkGroupSaveRespDTO;
import com.nageoffer.shortlink.admin.service.GroupService;
import com.nageoffer.shortlink.admin.util.RandomStringGenerator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    @Override
    public void saveGroup(String groupName) {
        //生成随机数
        String gid;
        while (true)
        {
            gid = RandomStringGenerator.generateRandomString(6);
            if (hasGid(gid) == false)
            {
                //不存在则退出
                break;
            }
        }
        GroupDO groupDO = GroupDO.builder()
                .gid(gid)
                .name(groupName)
                .username(UserContext.getUsername())
                .sortOrder(0)
                .build();
        baseMapper.insert(groupDO);
    }

    @Override
    public List<ShortLinkGroupSaveRespDTO> listGroup() {

        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(BaseDO::getDelFlag, 0)
                //TODO 这里还拿不到用户名，后续通过网关或者ThreadLocal
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .orderByDesc(GroupDO::getSortOrder, BaseDO::getUpdateTime);
        List<GroupDO> groupDOList = baseMapper.selectList(wrapper);
        List<ShortLinkGroupSaveRespDTO> groupSaveRespDTOS = BeanUtil.copyToList(groupDOList, ShortLinkGroupSaveRespDTO.class);
        return groupSaveRespDTOS;
    }

    public boolean hasGid(String gid)
    {
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                //TODO 这里还拿不到用户名，后续通过网关或者ThreadLocal
                .eq(GroupDO::getUsername, UserContext.getUsername());
        GroupDO groupDO = baseMapper.selectOne(wrapper);
        if(groupDO == null)
        {
            return false;
        }else {
            return true;
        }
    }
}
