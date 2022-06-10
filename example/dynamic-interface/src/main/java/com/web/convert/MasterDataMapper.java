package com.web.convert;

import com.web.entity.InterfaceEntity;
import com.web.entity.InterfaceParamEntity;
import com.web.entity.ModelEntity;
import com.web.entity.ModelTableColumnEntity;
import com.web.vo.InterfaceParamVO;
import com.web.vo.InterfaceVO;
import com.web.vo.ModelTableColumnVO;
import com.web.vo.ModelVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @ Author : wuheping
 * @ Date   : 2022/6/2
 * @ Desc   : 描述
 */
@Mapper
public interface MasterDataMapper {

    MasterDataMapper INSTANCES = Mappers.getMapper(MasterDataMapper.class);

    // 模型
    ModelTableColumnVO modelTableColumnEntity2VO(ModelTableColumnEntity modelTableColumnEntity);
    ModelTableColumnEntity modelTableColumnVO2Entity(ModelTableColumnVO modelTableColumnVO);
    ModelEntity modelVO2Entity(ModelVO modelVO);
    ModelVO modelEntity2VO(ModelEntity modelEntity);

    // 接口
    InterfaceEntity interfaceVO2Entity(InterfaceVO interfaceVO);
    InterfaceVO interfaceEntity2VO(InterfaceEntity interfaceEntity);
    List<InterfaceParamVO> interfaceParamEntitys2VOs(List<InterfaceParamEntity> interfaceParamEntities);

}
