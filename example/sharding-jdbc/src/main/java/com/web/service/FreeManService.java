package com.web.service;

import com.web.model.FreeManDO;
import com.web.repository.FreeManRepository;
import com.web.util.IdentityUtil;
import com.web.util.NameUtil;
import com.web.vo.freeman.FreeManQueryVO;
import com.web.vo.freeman.FreeManVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class FreeManService {

    @Autowired
    private FreeManRepository freeManRepository;

    /**
     * 随机添加一个人
     * @param id
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void addFreeMan(long id) throws Exception {
        String card = IdentityUtil.createIdentity();
        String sex = IdentityUtil.getGenderByIdCard(card);
        FreeManDO freeManDO = new FreeManDO();
        freeManDO.setId(id)
                .setIdentity(card)
                .setSex(sex)
                .setName(NameUtil.getChineseName(sex))
                .setAddress(IdentityUtil.getProvinceByIdCard(card))
                .setAge(IdentityUtil.getAgeByIdCard(card));
        freeManRepository.save(freeManDO);
        log.info("被执行:" + id);
    }

    /**
     * 身份证
     * @param freeManQueryVO
     * @return
     * @throws Exception
     */
    public FreeManVO findByIdentity(FreeManQueryVO freeManQueryVO) throws Exception{
        try {

            FreeManDO freeManDO = freeManRepository.findByIdentity(freeManQueryVO.getIdentity());
            FreeManVO freeManVO = new FreeManVO();
            BeanUtils.copyProperties(freeManDO, freeManVO);
            return freeManVO;
        } catch (Exception e) {
            log.error("method:findByIdentity 异常", e);
            throw new Exception(e.getMessage());
        }
    }
}
