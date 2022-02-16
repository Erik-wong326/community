package com.cqupt.community.controller;

import com.cqupt.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/14 15:17
 */
@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHi(){
        return "Hello Spring Boot";
    }

    //模拟处理查询请求的方法，体会 Controller 调用 service
    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return alphaService.find();
    }

    //Http演示
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        //获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ":" + value);
        }
        System.out.println(request.getParameter("code"));

        //返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try(PrintWriter writer = response.getWriter()) {
            writer.write("<h1>cqupt</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //简化方式处理 GET 请求
    //假设:查询学生信息
    //路径设计： /students?current=1&limit=20
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    //请求路径中没有 current 和 limit 怎么办 -> @RequestParam(参数名,required是否必须要求带这个参数,defaultValue参数默认值)
    public String getStudents(
            @RequestParam(name="current",required = false,defaultValue = "1")int current,
            @RequestParam(name = "limit",required = false,defaultValue = "10") int limit){
        //请求中的参数current limit怎么get -> int current, int limit
        System.out.println(current);
        System.out.println(limit);
        return "students information";
    }

    //假设:根据学生id查询一个学生
    // 路径设计: /student/123
    //演示参数是路径的一部分时，如何 get 参数  ->  @PathVariable
    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "a student.";
    }

    //POST 请求
    //GET请求 缺点：明文/数据量有限
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    //参数名与html表单input的名一致就会传递过来
    public String saveStudent(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    //服务器向浏览器 响应html数据 方式1 -> ModelAndView
    //假设:浏览器查询一个老师的信息
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView mav = new ModelAndView();
        mav.addObject("name","唐三藏");
        mav.addObject("age",30);
        mav.setViewName("/demo/view");
        return mav;
    }

    //响应html数据方式2 -> Model
    //假设:查询学校信息
    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","cqupt");
        model.addAttribute("age","70");
        return "/demo/view";
    }

    //方式1逻辑较清晰，方式2代码较为简洁

    // 响应JSON数据(异步请求)
    // Java对象 -> JSON字符串 -> JS对象
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody  //返回 JSON 需要加这个注解，否则会认为返回的是html
    public Map<String,Object> getEmp(){
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","猪八戒");
        emp.put("age","10");
        emp.put("salary","5000");
        return emp;
    }

    //场景:查询所有员工
    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody  //返回 JSON 需要加这个注解，否则会认为返回的是html
    public List<Map<String,Object>> getEmps(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","猪八戒");
        emp.put("age","10");
        emp.put("salary","5000");
        list.add(emp);
        emp = new HashMap<>();
        emp.put("name","孙悟空");
        emp.put("age","30");
        emp.put("salary","22000");
        list.add(emp);
        emp = new HashMap<>();
        emp.put("name","沙悟净");
        emp.put("age","20");
        emp.put("salary","2000");
        list.add(emp);
        return list;
    }
}
