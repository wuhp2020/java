package com.web.service;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.web.model.ImageDB;
import com.web.repository.ImageRepository;
import com.web.vo.image.ImageQueryVO;
import com.web.vo.image.ImageSaveVO;
import com.web.vo.image.ImageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    /**
     * 增
     * @param imageSaveVO
     * @return
     * @throws Exception
     */
    public ImageVO save(ImageSaveVO imageSaveVO) throws Exception{
        try {
            ImageDB imageDB = new ImageDB();
            imageDB.setCreateTime(new Date());
            imageDB.setImagebase64(imageSaveVO.getImagebase64());
            imageDB.setType(imageSaveVO.getType());
            imageDB.setImageId(UUID.randomUUID().toString().replaceAll("-", ""));
            imageDB = imageRepository.save(imageDB);
            ImageVO imageVO = new ImageVO();
            BeanUtils.copyProperties(imageDB, imageVO);
            return imageVO;
        } catch (Exception e) {
            log.error("method:save 异常", e);
            throw new Exception(e.getMessage());
        }

    }

    /**
     * 删
     * @param imageIds
     * @return
     */
    @Transactional
    public Pair<Boolean, String> delete(List<String> imageIds) {
        try {
            imageIds.stream().forEach(imageId -> imageRepository.deleteByImageId(imageId));
            return Pair.of(true, "OK");
        } catch (Exception e) {
            log.error("method:delete 异常", e);
            return Pair.of(false, e.getMessage());
        }

    }

    /**
     * 单个查询
     * @param imageId
     * @return
     * @throws Exception
     */
    public ImageVO findByImageId(String imageId) throws Exception{
        try {
            ImageDB imageDB = imageRepository.findByImageId(imageId);
            ImageVO imageVO = new ImageVO();
            BeanUtils.copyProperties(imageDB, imageVO);
            return imageVO;
        } catch (Exception e) {
            log.error("method:findOne 异常", e);
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 分页查询
     * @param imageQueryVO
     * @return
     * @throws Exception
     */
    public Page<ImageVO> findByPage(ImageQueryVO imageQueryVO) throws Exception{
        Pageable pageable = PageRequest.of(imageQueryVO.getPageNum(),
                imageQueryVO.getPageSize(), Sort.Direction.DESC, "createTime");
        Predicate predicate = new BooleanBuilder();
        Page<ImageDB> pageDOs =  imageRepository.findAll(predicate, pageable);
        List<ImageVO> imageVOs = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageDOs.getContent())) {
            List<ImageDB> imageDBs = pageDOs.getContent();
            imageDBs.stream().forEach(imageDB -> {
                ImageVO imageVO = new ImageVO();
                BeanUtils.copyProperties(imageDB, imageVO);
                imageVOs.add(imageVO);
            });
        }
        return new PageImpl<ImageVO>(imageVOs, pageable, pageDOs.getTotalElements());
    }

}
