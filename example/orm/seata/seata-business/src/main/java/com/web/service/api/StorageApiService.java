package com.web.service.api;

import com.web.vo.common.ResponseVO;
import com.web.vo.storage.StorageRequestVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "storage-service")
public interface StorageApiService {
    @PostMapping("/api/v1/storage/deduct")
    public ResponseVO deduct(@RequestBody StorageRequestVO storageRequestVO);
}
