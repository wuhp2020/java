package com.web.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.web.convert.MasterDataMapper;
import com.web.entity.ModelEntity;
import com.web.entity.ModelTableColumnEntity;
import com.web.entity.ModelTableEntity;
import com.web.mapper.ModelMapper;
import com.web.mapper.ModelTableColumnMapper;
import com.web.mapper.ModelTableMapper;
import com.web.vo.ModelTableColumnVO;
import com.web.vo.ModelVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class ModelService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ModelTableMapper modelTableMapper;

    @Autowired
    private ModelTableColumnMapper modelTableColumnMapper;


    public ModelTableColumnVO createTree(List<Long> tableIds, List<Long> modelIds) {

        if(CollectionUtils.isEmpty(tableIds) && CollectionUtils.isEmpty(modelIds)) {
            return null;
        }

        // 查询所有关联的字段
        Wrapper wrapper = Wrappers.<ModelTableColumnEntity>query().lambda()
                .in(CollectionUtils.isEmpty(tableIds)? false: true, ModelTableColumnEntity::getTableId, tableIds)
                .in(CollectionUtils.isEmpty(modelIds)? false: true, ModelTableColumnEntity::getModelId, modelIds);
        List<ModelTableColumnEntity> modelTableColumnEntities = modelTableColumnMapper.selectList(wrapper);
        Map<Long, ModelTableColumnVO> treeVoMap = Maps.newTreeMap();

        ModelTableColumnVO modelTableColumnVO = new ModelTableColumnVO();

        // 理论上只有一个主表, 其他都应该关联
        for (ModelTableColumnEntity treeVo : modelTableColumnEntities) {
            ModelTableColumnVO columnVO = MasterDataMapper.INSTANCES.modelTableColumnEntity2VO(treeVo);
            treeVoMap.put(treeVo.getId(), columnVO);
            if (StringUtils.isEmpty(treeVo.getParentId())) {
                modelTableColumnVO = columnVO;
            }
        }

        treeVoMap.forEach((s, modelTableColumn) -> {
            if (modelTableColumn.getParentId() != null) {
                ModelTableColumnVO parent = treeVoMap.get(modelTableColumn.getParentId());
                if (parent != null) {
                    parent.getChildren().add(modelTableColumn);
                }
            }
        });

        return modelTableColumnVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(ModelVO modelVO) {

        ModelEntity modelEntity = MasterDataMapper.INSTANCES.modelVO2Entity(modelVO);
        Long id = SnowflakeIdWorker.getId();
        modelEntity.setId(id);
        modelMapper.insert(modelEntity);

        ModelTableColumnVO column = modelVO.getColumn();
        Long tableId = SnowflakeIdWorker.getId();
        ModelTableEntity modelTableEntity = new ModelTableEntity();
        modelTableEntity.setModelId(id);
        modelTableEntity.setId(tableId);
        modelTableEntity.setName(column.getName());
        modelTableMapper.insert(modelTableEntity);

        ModelTableColumnEntity modelTableColumnEntity = MasterDataMapper.INSTANCES.modelTableColumnVO2Entity(column);
        Long modelTableColumnId = SnowflakeIdWorker.getId();
        modelTableColumnEntity.setId(modelTableColumnId);
        modelTableColumnEntity.setModelId(id);
        modelTableColumnEntity.setTableId(tableId);
        modelTableColumnMapper.insert(modelTableColumnEntity);

        this.childrenModelTableColumn(id, tableId, modelTableColumnId, column.getChildren());
    }

    private void childrenModelTableColumn(Long modelId, Long tableId, Long parentId, List<ModelTableColumnVO> children) {

        for (ModelTableColumnVO tableColumnVO: children) {
            if ("Object".equals(tableColumnVO.getType())) {

                ModelTableEntity modelTableEntity = new ModelTableEntity();
                modelTableEntity.setModelId(modelId);
                Long objTableId = SnowflakeIdWorker.getId();
                modelTableEntity.setId(objTableId);
                modelTableEntity.setName(tableColumnVO.getName());
                modelTableMapper.insert(modelTableEntity);

                ModelTableColumnEntity modelTableColumnEntity = MasterDataMapper.INSTANCES.modelTableColumnVO2Entity(tableColumnVO);
                Long objParentId = SnowflakeIdWorker.getId();
                modelTableColumnEntity.setId(objParentId);
                modelTableColumnEntity.setModelId(modelId);
                modelTableColumnEntity.setTableId(tableId);
                modelTableColumnEntity.setParentId(parentId);
                modelTableColumnMapper.insert(modelTableColumnEntity);

                this.childrenModelTableColumn(modelId, objTableId, objParentId, tableColumnVO.getChildren());
            } else {
                ModelTableColumnEntity modelTableColumnEntity = MasterDataMapper.INSTANCES.modelTableColumnVO2Entity(tableColumnVO);
                modelTableColumnEntity.setId(SnowflakeIdWorker.getId());
                modelTableColumnEntity.setModelId(modelId);
                modelTableColumnEntity.setTableId(tableId);
                modelTableColumnEntity.setParentId(parentId);
                modelTableColumnMapper.insert(modelTableColumnEntity);
            }
        }
    }

    public ModelVO getById(Long id) {
        ModelEntity modelEntity = modelMapper.selectById(id);
        ModelVO modelVO = MasterDataMapper.INSTANCES.modelEntity2VO(modelEntity);
        List<Long> modelIds = Lists.newArrayList();
        modelIds.add(id);
        ModelTableColumnVO modelTableColumnVO = this.createTree(null, modelIds);
        modelVO.setColumn(modelTableColumnVO);
        return modelVO;
    }
}
