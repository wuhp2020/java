package com.web.convert;

import com.web.entity.InterfaceEntity;
import com.web.entity.InterfaceObjectParamRelationEntity;
import com.web.entity.InterfaceParamEntity;
import com.web.vo.InterfaceObjectParamRelationReqVO;
import com.web.vo.InterfaceObjectParamRelationResVO;
import com.web.vo.InterfaceParamReqVO;
import com.web.vo.InterfaceParamResVO;
import com.web.vo.InterfaceReqVO;
import com.web.vo.InterfaceResVO;
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

    // 接口管理
    InterfaceEntity masterInterfaceReqVO2Entity(InterfaceReqVO reqVO);
    InterfaceResVO masterInterfaceEntity2VO(InterfaceEntity entity);
    InterfaceParamEntity masterInterfaceParamReqVO2Entity(InterfaceParamReqVO reqVO);
    List<InterfaceParamResVO> masterInterfaceParamEntitys2VO(List<InterfaceParamEntity> entitys);
    InterfaceParamResVO masterInterfaceParamEntity2VO(InterfaceParamEntity entity);
    InterfaceObjectParamRelationResVO masterInterfaceObjectParamRelationEntity2VO(InterfaceObjectParamRelationEntity entity);
    List<InterfaceObjectParamRelationResVO> masterInterfaceObjectParamRelationEntitys2VO(List<InterfaceObjectParamRelationEntity> entities);
    InterfaceObjectParamRelationEntity masterInterfaceObjectParamRelationReqVO2Entity(InterfaceObjectParamRelationReqVO reqVO);


}
