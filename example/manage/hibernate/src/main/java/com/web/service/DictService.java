package com.web.service;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.web.model.DictDO;
import com.web.repository.DictRepository;
import com.web.vo.dict.DictQueryVO;
import com.web.vo.dict.DictVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Slf4j
public class DictService {

    @Autowired
    private DictRepository dictRepository;

    /**
     * 增
     * @param dictVO
     * @return
     * @throws Exception
     */
    public DictVO save(DictVO dictVO) throws Exception{
        DictDO dictDO = new DictDO();
        BeanUtils.copyProperties(dictVO, dictDO);
        dictDO = dictRepository.save(dictDO);
        dictVO.setId(dictDO.getId().toString());
        return dictVO;
    }

    /**
     * 删
     * @param ids
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Pair<Boolean, String> delete(List<String> ids) {
        ids.stream().forEach(id -> dictRepository.deleteById(id));
        return Pair.of(true, "OK");
    }

    /**
     * 改
     * @param dictVO
     * @return
     * @throws Exception
     */
    @Transactional
    public DictVO update(DictVO dictVO) throws Exception{
        dictRepository.deleteById(dictVO.getId());
        return this.save(dictVO);
    }

    /**
     * 单个查询
     * @param id
     * @return
     * @throws Exception
     */
    public DictVO findOne(String id) throws Exception{
        DictDO dictDO = dictRepository.findById(id).get();
        DictVO dictVO = new DictVO();
        BeanUtils.copyProperties(dictDO, dictVO);
        return dictVO;
    }

    /**
     * 分页查询
     * @param dictQueryVO
     * @return
     * @throws Exception
     */
    public Page<DictVO> findByPage(DictQueryVO dictQueryVO) throws Exception{
        Pageable pageable = PageRequest.of(dictQueryVO.getPageNum(),
                dictQueryVO.getPageSize(), Sort.Direction.DESC, "id");
        Predicate predicate = dictQueryVO.buildDict();
        Page<DictDO> pageDOs = dictRepository.findAll(predicate, pageable);
                List<DictVO> dictVOs = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageDOs.getContent())) {
            List<DictDO> dictDOs = pageDOs.getContent();
            dictDOs.stream().forEach(dictDOResult -> {
                DictVO dictVO = new DictVO();
                BeanUtils.copyProperties(dictDOResult, dictVO);
                dictVOs.add(dictVO);
            });
        }
        return new PageImpl<DictVO>(dictVOs, pageable, pageDOs.getTotalElements());
    }

    /**
     * 通过code 和 Type查询
     * @param dictVO
     * @return
     */
    public List<DictDO> findByCodeAndType(DictVO dictVO) throws Exception {
        List<DictDO> dictDOs = dictRepository.findByCodeAndType(dictVO.getType(), dictVO.getCode());
        return dictDOs;
    }

    /**
     * 根据code 和 type 重复校验
     * @param dictVO
     * @return
     */
    public Pair<Boolean, String> checkRepeat(DictVO dictVO) throws Exception{
        List<DictDO> dictDOs = this.findByCodeAndType(dictVO);
        if (!CollectionUtils.isEmpty(dictDOs)) {
            return Pair.of(false, "type: "+ dictVO.getType() + ", code: "+ dictVO.getCode() + " 已经存在");
        }
        return Pair.of(true, "OK");
    }
}
