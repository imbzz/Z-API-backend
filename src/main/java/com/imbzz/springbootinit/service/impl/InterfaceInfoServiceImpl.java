package com.imbzz.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imbzz.springbootinit.common.ErrorCode;
import com.imbzz.springbootinit.constant.CommonConstant;
import com.imbzz.springbootinit.exception.BusinessException;
import com.imbzz.springbootinit.exception.ThrowUtils;
import com.imbzz.springbootinit.mapper.InterfaceInfoMapper;
import com.imbzz.springbootinit.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.imbzz.springbootinit.model.entity.*;
import com.imbzz.springbootinit.model.enums.InterfaceInfoEnum;
import com.imbzz.springbootinit.model.vo.InterfaceInfoVO;
import com.imbzz.springbootinit.model.vo.PostVO;
import com.imbzz.springbootinit.model.vo.UserVO;
import com.imbzz.springbootinit.service.InterfaceInfoService;
import com.imbzz.springbootinit.service.UserService;
import com.imbzz.springbootinit.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 86188
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2023-11-12 17:22:45
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Resource
    private UserService userService;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestheader = interfaceInfo.getRequestHeader();
        String responseheader = interfaceInfo.getResponseHeader();
        Integer status = interfaceInfo.getStatus();
        String method = interfaceInfo.getMethod();
        Long userId = interfaceInfo.getUserId();
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建时，参数不能为空
        if (add) {
            // todo 暂时去掉userId和status的校验，后续再加上
//            ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR);
//            ThrowUtils.throwIf(InterfaceInfoEnum.getEnumByValue(status) != null, ErrorCode.PARAMS_ERROR);
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, description, url, requestheader, responseheader, method), ErrorCode.PARAMS_ERROR);
        }
        ThrowUtils.throwIf(StringUtils.isBlank(name) && name.length() > 50, ErrorCode.PARAMS_ERROR);
        if (!add) {
            ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        }
    }

    /**
     * 获取查询包装类
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String url = interfaceInfoQueryRequest.getUrl();
        String requestheader = interfaceInfoQueryRequest.getRequestheader();
        String responseheader = interfaceInfoQueryRequest.getResponseheader();
        Integer status = interfaceInfoQueryRequest.getStatus();
        String method = interfaceInfoQueryRequest.getMethod();
        Long userId = interfaceInfoQueryRequest.getUserId();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String sortField = interfaceInfoQueryRequest.getSortField();

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceInfoQueryRequest == null) {
            return queryWrapper;
        }

        // 拼接查询条件
        if (StringUtils.isNotBlank(description)) {
            queryWrapper.like("description", description).or().like("description", description);
        }
        queryWrapper.like(StringUtils.isNotBlank(name) , "title", name);
        queryWrapper.like(StringUtils.isNotBlank(url), "content", url);
        queryWrapper.ne(ObjectUtils.isNotEmpty(requestheader), "requestheader", requestheader);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id)&&id > 0 , "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId) && userId >0, "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(responseheader), "responseheader", responseheader);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status) && status > 0, "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(method), "method", method);

//        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

//    @Override
//    public Page<InterfaceInfo> searchFromEs(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
//     Long id = interfaceInfoQueryRequest.getId();
//     String name = interfaceInfoQueryRequest.getName();
//     String description = interfaceInfoQueryRequest.getDescription();
//     String url = interfaceInfoQueryRequest.getUrl();
//     String requestheader = interfaceInfoQueryRequest.getRequestheader();
//     String responseheader = interfaceInfoQueryRequest.getResponseheader();
//     Integer status = interfaceInfoQueryRequest.getStatus();
//     String method = interfaceInfoQueryRequest.getMethod();
//     Long userId = interfaceInfoQueryRequest.getUserId();
//        Long notId = interfaceInfoQueryRequest.getNotId();
//        String searchText = interfaceInfoQueryRequest.getSearchText();
//        // es 起始页为 0
//        long current = interfaceInfoQueryRequest.getCurrent() - 1;
//        long pageSize = interfaceInfoQueryRequest.getPageSize();
//        String sortField = interfaceInfoQueryRequest.getSortField();
//        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        // 过滤
//        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete", 0));
//        if (id != null) {
//            boolQueryBuilder.filter(QueryBuilders.termQuery("id",  id));
//        }
//        if (notId != null) {
//            boolQueryBuilder.mustNot(QueryBuilders.termQuery("id", notId));
//        }
//        if (userId != null) {
//            boolQueryBuilder.filter(QueryBuilders.termQuery("userId", userId));
//        }
//        // 包含任何一个标签即可
//        // 按关键词检索
//        if (StringUtils.isNotBlank(searchText)) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("name", searchText));
//            boolQueryBuilder.should(QueryBuilders.matchQuery("description", searchText));
//            boolQueryBuilder.minimumShouldMatch(1);
//        }
//        // 按标题检索
//        if (StringUtils.isNotBlank(name)) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("name", name));
//            boolQueryBuilder.minimumShouldMatch(1);
//        }
//        // 按内容检索
//        if (StringUtils.isNotBlank(description)) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("description", description));
//            boolQueryBuilder.minimumShouldMatch(1);
//        }
//        // 排序
//        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
//        if (StringUtils.isNotBlank(sortField)) {
//            sortBuilder = SortBuilders.fieldSort(sortField);
//            sortBuilder.order(CommonConstant.SORT_ORDER_ASC.equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);
//        }
//        // 分页
//        PageRequest pageRequest = PageRequest.of((int) current, (int) pageSize);
//        // 构造查询
//        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
//                .withPageable(pageRequest).withSorts(sortBuilder).build();
//        SearchHits<InterfaceInfoEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, InterfaceInfoEsDTO.class);
//        Page<InterfaceInfo> page = new Page<>();
//        page.setTotal(searchHits.getTotalHits());
//        List<InterfaceInfo> resourceList = new ArrayList<>();
//        // 查出结果后，从 db 获取最新动态数据（比如点赞数）
//        if (searchHits.hasSearchHits()) {
//            List<SearchHit<InterfaceInfoEsDTO>> searchHitList = searchHits.getSearchHits();
//            List<Long> interfaceInfoIdList = searchHitList.stream().map(searchHit -> searchHit.getContent().getId())
//                    .collect(Collectors.toList());
//            List<InterfaceInfo> interfaceInfoList = baseMapper.selectBatchIds(interfaceInfoIdList);
//            if (interfaceInfoList != null) {
//                Map<Long, List<InterfaceInfo>> idInterfaceInfoMap = interfaceInfoList.stream().collect(Collectors.groupingBy(InterfaceInfo::getId));
//                interfaceInfoIdList.forEach(interfaceInfoId -> {
//                    if (idInterfaceInfoMap.containsKey(interfaceInfoId)) {
//                        resourceList.add(idInterfaceInfoMap.get(interfaceInfoId).get(0));
//                    } else {
//                        // 从 es 清空 db 已物理删除的数据
//                        String delete = elasticsearchRestTemplate.delete(String.valueOf(interfaceInfoId), InterfaceInfoEsDTO.class);
//                        log.info("delete interfaceInfo {}", delete);
//                    }
//                });
//            }
//        }
//        page.setRecords(resourceList);
//        return page;
//    }

    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request) {
        return null;
    }

    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request) {
        List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
        Page<InterfaceInfoVO> interfaceInfoListPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(), interfaceInfoPage.getTotal());
        if (CollectionUtils.isEmpty(interfaceInfoList)) {
            return interfaceInfoListPage;
        }
        List<InterfaceInfoVO> collect = interfaceInfoPage.getRecords().stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        interfaceInfoListPage.setRecords(collect);
        return interfaceInfoListPage;
    }
}




