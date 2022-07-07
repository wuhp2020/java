package com.web.repository;

import com.web.model.ImageDB;
import org.bson.types.ObjectId;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository
        extends CrudRepository<ImageDB, ObjectId>, QuerydslPredicateExecutor<ImageDB> {

    public ImageDB findByImageId(String imageId);

    public void deleteByImageId(String imageId);
}
