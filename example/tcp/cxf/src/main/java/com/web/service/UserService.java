package com.web.service;

import com.api.vo.response.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import javax.jws.WebService;

@Slf4j
@WebService
@Service
public class UserService {

    @WebMethod(operationName = "findOne")
    public UserVO findOne(Long id) throws Exception{
        try {
            UserVO userVO = new UserVO();
            return userVO;
        } catch (Exception e) {
            log.error("method:findOne 异常", e);
            throw new Exception(e.getMessage());
        }
    }
}
