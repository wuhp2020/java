package com.web.service;

import com.web.model.StorageDO;
import com.web.repository.StorageRepository;
import com.web.vo.storage.StorageRequestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    @Transactional
    public void deduct(StorageRequestVO storageRequestVO) {
        StorageDO storage = storageRepository.findByCommodityCode(storageRequestVO.getCommodityCode()).get(0);
        storage.setCount(storage.getCount() - storageRequestVO.getOrderCount());
        storageRepository.save(storage);
    }
}
