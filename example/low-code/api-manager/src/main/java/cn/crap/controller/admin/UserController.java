package cn.crap.controller.admin;

import cn.crap.adapter.UserAdapter;
import cn.crap.dto.LoginInfoDto;
import cn.crap.dto.UserDto;
import cn.crap.enu.LoginType;
import cn.crap.enu.MyError;
import cn.crap.enu.UserStatus;
import cn.crap.enu.UserType;
import cn.crap.framework.JsonResult;
import cn.crap.framework.MyException;
import cn.crap.framework.base.BaseController;
import cn.crap.framework.interceptor.AuthPassport;
import cn.crap.model.User;
import cn.crap.model.UserCriteria;
import cn.crap.query.UserQuery;
import cn.crap.service.ProjectService;
import cn.crap.service.ProjectUserService;
import cn.crap.service.UserService;
import cn.crap.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private UserService customUserService;

    @RequestMapping("/list.do")
    @ResponseBody
    @AuthPassport(authority = C_AUTH_USER)
    public JsonResult list(@ModelAttribute UserQuery query) throws MyException{
        Page page = new Page(query);

        page.setAllRow(userService.count(query));
        return new JsonResult(1, UserAdapter.getDto(userService.query(query)), page);
    }

    @RequestMapping("/detail.do")
    @ResponseBody
    @AuthPassport(authority = C_AUTH_USER)
    public JsonResult detail(String id) {
        User user = new User();
        if (id != null) {
            user = userService.getById(id);
        }
        return new JsonResult().data(UserAdapter.getDto(user));
    }

    @RequestMapping("/addOrUpdate.do")
    @ResponseBody
    @AuthPassport(authority = C_AUTH_USER)
    public JsonResult add(@ModelAttribute UserDto userDto, String password) throws MyException {
        // ????????????
        if (MyString.isEmpty(userDto.getEmail()) || !Tools.checkEmail(userDto.getEmail())) {
            throw new MyException(MyError.E000032);
        }

        User user = UserAdapter.getModel(userDto);
        if (MyString.isNotEmpty(password)){
            user.setPassword(password);
        }

        UserQuery query = new UserQuery().setEqualEmail(userDto.getEmail().toLowerCase());
        if (MyString.isEmpty(userDto.getId())){
            if(userService.count(query) > 0){
                throw new MyException(MyError.E000065, "??????????????????");
            }
            return addUser(user);
        }else{
            User dbUser = userService.getById(user.getId());
            if(!dbUser.getEmail().equalsIgnoreCase(userDto.getEmail()) && userService.count(query) > 0){
                throw new MyException(MyError.E000065, "??????????????????");
            }
            return updateUser(user);
        }
    }

    private JsonResult addUser(@ModelAttribute User user) throws MyException {
        if (user.getUserName().isEmpty() || !Tools.checkUserName(user.getUserName()) || ADMIN.equals(user.getUserName())) {
            throw new MyException(MyError.E000028);
        }

        // ??????????????????
        UserQuery query = new UserQuery().setEqualUserName(user.getUserName()).setPageSize(1);
        int userSize = userService.count(query);
        if (userSize > 0) {
            throw new MyException(MyError.E000015);
        }

        if (MyString.isEmpty(user.getPassword())) {
            throw new MyException(MyError.E000061);
        }

        LoginInfoDto loginUser = LoginUserHelper.getUser();
        // ????????????????????????????????????????????????????????????
        if (!Tools.isSuperAdmin(loginUser.getAuthStr())) {
            user.setAuth("");
            user.setAuthName("");
            user.setType(UserType.USER.getType());// ????????????
        }
        user.setStatus(UserStatus.INVALID.getType());
        user.setPasswordSalt(Tools.getChar(20));
        user.setLoginType(LoginType.COMMON.getValue());
        user.setPassword(MD5.encrytMD5(user.getPassword(), user.getPasswordSalt()));

        userService.insert(user);
        return new JsonResult(1, UserAdapter.getDto(user));
    }

    private JsonResult updateUser(@ModelAttribute User user) throws MyException {
        Assert.notNull(user,"user????????????");
        Assert.notNull(user.getId(), "user.id????????????");
        // ??????????????????
        
        if (customUserService.countByNameExceptUserId(user.getUserName(), user.getId()) > 0) {
            throw new MyException(MyError.E000015);
        }

        if (user.getUserName().isEmpty() || !Tools.checkUserName(user.getUserName())) {
            throw new MyException(MyError.E000028);
        }

        User dbUser = userService.getById(user.getId());
        if (dbUser == null) {
            throw new MyException(MyError.E000013);
        }

        LoginInfoDto loginUser = LoginUserHelper.getUser();
        // ???????????????????????????????????????????????????????????????????????????????????????admin?????????????????????????????????????????????????????????
        if (Tools.isSuperAdmin(dbUser.getRoleId())) {
            if (!dbUser.getId().equals(loginUser.getId()) && !loginUser.getUserName().equals("admin")) {
                throw new MyException(MyError.E000053);
            }
        }

        // admin ????????????????????????
        if (dbUser.getUserName().equals("admin") && !user.getUserName().equals("admin")) {
            throw new MyException(MyError.E000055);
        }

        // ??????????????????????????????????????????
        if (!Tools.isSuperAdmin(loginUser.getAuthStr())) {
            if (!dbUser.getId().equals(loginUser.getId()) && dbUser.getType() == UserType.ADMIN.getType()) {
                throw new MyException(MyError.E000054);
            }
        }

        // ????????????????????????????????????????????????????????????
        if (!Tools.isSuperAdmin(loginUser.getAuthStr())) {
            user.setAuth(null);
            user.setAuthName(null);
            user.setType(null);
        }

        // ???????????????????????????????????????????????????
        if (MyString.isEmpty(dbUser.getEmail()) || !user.getEmail().equals(dbUser.getEmail())) {
            user.setStatus(UserStatus.INVALID.getType());
            dbUser.setEmail(user.getEmail());
            dbUser.setStatus(UserStatus.INVALID.getType());
            userCache.add(user.getId(), new LoginInfoDto(dbUser));
        }

        // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (!MyString.isEmpty(user.getPassword())) {
            user.setPasswordSalt(Tools.getChar(20));
            user.setPassword(MD5.encrytMD5(user.getPassword(), user.getPasswordSalt()));
            user.setLoginType(LoginType.COMMON.getValue());
        }

        UserCriteria example = new UserCriteria();
        example.createCriteria().andEmailEqualTo(user.getEmail()).andLoginTypeEqualTo(user.getLoginType()).andIdNotEqualTo(dbUser.getId());
        int userSize = userService.countByExample(example);
        if (userSize > 0){
            throw new MyException(MyError.E000062);
        }

        userService.update(user);
        user.setPassword(null);
        return new JsonResult(1, UserAdapter.getDto(user));
    }
}
