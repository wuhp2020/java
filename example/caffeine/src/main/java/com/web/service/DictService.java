package com.web.service;

import com.google.common.collect.Lists;
import com.web.vo.dict.DictQueryVO;
import com.web.vo.dict.DictVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@CacheConfig(cacheNames = "caffeineCacheManager")
public class DictService {

    /**
     * 分页查询
     * @param dictQueryVO
     * @return
     * @throws Exception
     */
    @Cacheable(cacheNames = "findByPage")
    public Page<DictVO> findByPage(DictQueryVO dictQueryVO) throws Exception {
        List<DictVO> list = Lists.newArrayList();
        DictVO dictVO1 = new DictVO();
        dictVO1.setId("1").setCode("1").setName("1").setType("1");
        list.add(dictVO1);

        DictVO dictVO2 = new DictVO();
        dictVO2.setId("2").setCode("2").setName("2").setType("2");
        list.add(dictVO2);

        DictVO dictVO3 = new DictVO();
        dictVO3.setId("3").setCode("3").setName("3").setType("3");
        list.add(dictVO3);

        DictVO dictVO4 = new DictVO();
        dictVO4.setId("4").setCode("4").setName("4").setType("4");
        list.add(dictVO4);

        DictVO dictVO5 = new DictVO();
        dictVO5.setId("5").setCode("5").setName("5").setType("5");
        list.add(dictVO5);
        Pageable pageable = PageRequest.of(dictQueryVO.getPageNum(),
                dictQueryVO.getPageSize(), Sort.Direction.DESC, "id");
        return new PageImpl<DictVO>(list, pageable, list.size());
    }

}
