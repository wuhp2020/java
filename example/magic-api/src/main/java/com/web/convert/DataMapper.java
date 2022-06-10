package com.web.convert;

import com.web.vo.api.CreateApiVO;
import com.web.vo.api.CreateGroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.magicapi.core.model.Group;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/26
 * @ Desc   : 描述
 */
@Mapper
public interface DataMapper {
    DataMapper INSTANCES = Mappers.getMapper(DataMapper.class);

    ApiInfo create2ApiInfo(CreateApiVO createApiVO);
    Group create2Group(CreateGroupVO createGroupVO);
}
