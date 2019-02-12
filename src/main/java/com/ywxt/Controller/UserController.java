package com.ywxt.Controller;

import com.alibaba.fastjson.JSONObject;
import com.ywxt.Annotation.PassToken;
import com.ywxt.Domain.User;
import com.ywxt.Service.Impl.UserServiceImpl;
import com.ywxt.Service.RoleService;
import com.ywxt.Service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;


@Validated
@Controller
@RequestMapping("/user")
public class UserController extends CommonController {
    @Autowired
    private UserService userService;


    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    @ResponseBody
    @PassToken
    public JSONObject login(@NotBlank @RequestParam("username") String username, @NotBlank @RequestParam("password") String password) throws Exception {
        String authToken = new UserServiceImpl().login(username, password);
        return this.returnObject(new HashMap<String, Object>() {{
            put("token", authToken);
        }});
    }

    @RequestMapping(value = {"/info"}, method = RequestMethod.GET)
    @ResponseBody
    public User info(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.getUserFromAuthToken(request);
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
    @ResponseBody
    public JSONObject logout(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");
        new UserServiceImpl().logout(authToken);
        return this.returnObject(new HashMap<String, Object>() {{
        }});
    }

    @RequestMapping(value = {"/test"}, method = RequestMethod.GET)
    @ResponseBody
    @PassToken
    public void test() {
        System.out.println(22222222);
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String requestUrl = request.getScheme()+"://" + request.getServerName()+ ":" + request.getServerPort() + request.getContextPath() + request.getServletPath() + (StringUtils.isBlank(request.getQueryString())?"":("?"+request.getQueryString())); //请求参
        } catch (NullPointerException e) {
            System.out.println(11111111);
            System.out.println(e.getClass());
        }
        System.out.println(333333);
    }

/*
* 查询所有用户
* */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public List<User> list(){
            List<User>users =userService.list();
        for (User user1 : users) {
            System.out.println(user1);
        }
            return users;
    }
/*
* 添加用户
* */
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public void add(@NotBlank @RequestParam("username") String username, @NotBlank @RequestParam("password") String password){

            Long account=  userService.add(username,password);

    }
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public void add(@RequestBody User user){
        userService.add(user);
        System.out.println(user);
    }
    /*
    * 根据id查询用户
    * */
  /*  @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @ResponseBody
    public User findUserById(@PathVariable(value = "id")long id){
       return userService.getUserById(id);
    }*/

    @RequestMapping(value = "/id",method = RequestMethod.GET)
    @ResponseBody
    public User findUserById(@RequestParam long id){
        return userService.getUserById(id);
    }

    /***
     * 修改用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public void modify(@RequestBody User user){
        //根据id修改user信息
      userService.updateUserById(user);
    }

   /*@RequestMapping(value = "/modifacation/{id}",method = RequestMethod.PUT)
   public void update(@RequestBody User user,@PathVariable long id){
       //根据id修改user信息
       userService.updateUserById(user,id);
   }*/
    /*
     * 根据id删除用户
     * */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public void deleteById(@RequestParam long id){
        userService.deleteById(id);
    }
}
