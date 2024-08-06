package com.nageoffer.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nageoffer.shortlink.project.common.convention.exception.ServiceException;
import com.nageoffer.shortlink.project.common.database.BaseDO;
import com.nageoffer.shortlink.project.config.RBloomFilterConfiguration;
import com.nageoffer.shortlink.project.dao.entity.ShortLinkDO;
import com.nageoffer.shortlink.project.dao.mapper.ShortLinkMapper;
import com.nageoffer.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.nageoffer.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.nageoffer.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.nageoffer.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.nageoffer.shortlink.project.service.ShortLinkService;
import com.nageoffer.shortlink.project.util.HashUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.stereotype.Service;

/**
 * 短链接接口实现层
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper , ShortLinkDO> implements ShortLinkService{

    private final RBloomFilter<String> stringRBloomFilter;

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO createReqDTO) {
        //生成短链接后缀
        String shortLinkSuffix = generateSuffix(createReqDTO);
        String fullShortLinkUrl = createReqDTO.getDomain() + "/" + shortLinkSuffix;
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .domain(createReqDTO.getDomain())
                .originUrl(createReqDTO.getOriginUrl())
                .gid(createReqDTO.getGid())
                .createdType(createReqDTO.getCreatedType())
                .validDateType(createReqDTO.getValidDateType())
                .validDate(createReqDTO.getValidDate())
                .describe(createReqDTO.getDescribe())
                .shortUri(shortLinkSuffix)
                .enableStatus(0)
                .fullShortUrl(fullShortLinkUrl)
                // 这里记得要将 getFavicon(requestParam.getOriginUrl()) 去掉，性能賊慢
                .build();
        //插入数据
        baseMapper.insert(shortLinkDO);
        //插入布隆过滤器
        stringRBloomFilter.add(fullShortLinkUrl);
        ShortLinkCreateRespDTO shortLinkCreateRespDTO = ShortLinkCreateRespDTO.builder()
                .fullShortUrl(shortLinkDO.getFullShortUrl())
                .gid(createReqDTO.getGid())
                .originUrl(createReqDTO.getOriginUrl())
                .build();
        return shortLinkCreateRespDTO;
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO reqDTO) {
        LambdaQueryWrapper<ShortLinkDO> wrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, reqDTO.getGid())
                .eq(BaseDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0);
        IPage<ShortLinkDO> resultPage = baseMapper.selectPage(reqDTO, wrapper);
        return resultPage.convert(each -> BeanUtil.toBean(each , ShortLinkPageRespDTO.class));

    }


    private String generateSuffix(ShortLinkCreateReqDTO shortLinkCreateReqDTO)
    {
        //设置最大循环数
        int customGenerateCount = 0;
        String shortUri;
        while (true)
        {
            if (customGenerateCount > 10)
            {
                throw new ServiceException("短链接频繁生成，请稍后重试");
            }
            //拿到原始链接
            String originUrl = shortLinkCreateReqDTO.getOriginUrl();
            //加上当前时间的毫秒数，同一个原始连接可以创建多个短链接
            originUrl += System.currentTimeMillis();
            //将原始链接转为短链接后缀
            shortUri = HashUtil.hashToBase62(originUrl);
            //从数据库查是否存在
            /*LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, shortLinkCreateReqDTO.getDomain() + "/" + shortUri);
            ShortLinkDO shortLinkDO = baseMapper.selectOne(queryWrapper);*/
            //从布隆过滤器中判断是否存在

            boolean contains = stringRBloomFilter.contains(shortLinkCreateReqDTO.getDomain() + "/" + shortUri);
            if (!contains)
            {
                //不存在则跳出
                break;
            }
            customGenerateCount++;
        }
        return shortUri;
    }
}