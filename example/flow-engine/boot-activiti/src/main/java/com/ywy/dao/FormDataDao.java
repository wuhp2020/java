package com.ywy.dao;

import com.ywy.entity.FormDataEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 表单业务数据Dao
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-07 18:13
 */
@Mapper
public interface FormDataDao {
    /**
     * 保持表单数据
     * @param users
     * @return
     */
    @Insert("<script>insert into form_data(proc_def_id, proc_inst_id, form_key, control_id, control_value)" +
            " values" +
            " <foreach collection='list' item='formData' index='index' separator=','>" +
            "(#{formData.procDefId}, #{formData.procInstId}, #{formData.formKey}, #{formData.controlId}, #{formData.controlValue})" +
            "</foreach>" +
            "</script>")
    int insertFormData(@Param("list")List<FormDataEntity> users);

    /**
     * 查询表单
     * @param procInstId
     * @return
     */
    @Select("select control_id, control_value from form_data where proc_inst_id = #{procInstId}")
    List<FormDataEntity> selectFormData(String procInstId);
}
