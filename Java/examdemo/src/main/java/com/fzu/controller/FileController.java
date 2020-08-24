package com.fzu.controller;
import com.fzu.pojo.QTable;
import com.fzu.pojo.STable;

import com.fzu.service.AdminService;
import com.fzu.service.AdminServiceImpl;
import com.fzu.pojo.TTable;
import com.fzu.service.StudentService;
import com.fzu.service.StudentServiceImpl;
import com.fzu.service.TeacherService;
import com.fzu.service.TeacherServiceImpl;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.DateFormatter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class FileController {

    @Autowired
    StudentService studentService=new StudentServiceImpl();
    @Autowired
    AdminService adminService=new AdminServiceImpl();
    @Autowired
    TeacherService teacherService=new TeacherServiceImpl();


    @ResponseBody
    @RequestMapping("/doImportStudentExcel")
    public Map<String, Object> StudentUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        String fileName = "";
        Cookie[]cookies= request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            System.out.println(i+"--"+cookies[i].getValue());
        }
        String teacherId = "";
        for (Cookie cookie : cookies) {
            if(cookie.getValue().charAt(0)=='T'){
                teacherId = cookie.getValue();
            }
        }
        String path = "";
        try {
            //将当前上下文初始化给  CommonsMutipartResolver
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //检查form中是否有enctype="multipart/form-data"
            if (multipartResolver.isMultipart(request)) {
                //将request变成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                //获取multiRequest 中所有的文件名
                Iterator iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    //一次遍历所有文件
                    MultipartFile file1 = multiRequest.getFile(iter.next().toString());
                    if (file != null) {
                        //获取上传文件名
                        fileName = file1.getOriginalFilename();
                        //获取后缀名
                        String sname = fileName.substring(fileName.lastIndexOf("."));
                        //时间格式化格式
                        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMddHHmmssSSS");
                        //获取当前时间并作为时间戳
                        String timeStamp=simpleDateFormat.format(new Date());
                        //拼接新的文件名
                        String newName ="学生表"+timeStamp+sname;//命名+时间戳+后缀
                        //指定上传文件的路径
                        //"C:/Users/11139/examsystem/"
                        path = "S:/sqlTest/examsystem/" + newName;
                        //上传保存
                        /* file.transferTo();*/
                        FileUtils.copyInputStreamToFile(file.getInputStream(),new File("S:/sqlTest/examsystem/",newName));
                        ////保存当前文件路径
                        //request.getSession().setAttribute("currFilePath", path);
                        //request.getSession().setAttribute("teacherId", teacherId);
                    }
                }
            }

            result.put("statusCode", "200");
            result.put("status",1);
            result.put("message", "上传成功!");
            result.put("filename", fileName);
        } catch (Exception ex) {
            result.put("statusCode", "300");
            result.put("status",0);
            result.put("message", "上传失败:" + ex.getMessage());
        }
        parseStudentExcel(request, path, teacherId);

        return result;
    }


    @ResponseBody
    @RequestMapping("/parseStudentExcel")
    public Map<String, Object> parseStudentExcel(HttpServletRequest request, String path, String teacherId) {
        Map<String, Object> result = new HashMap<String, Object>();
        Workbook workbook = null;
        ////获取文件路径
        //String path =(String)request.getSession().getAttribute("currFilePath");
        System.out.println("测试路径"+path);//测试看看路径正确？
        //获取文件格式
        String fileType = path.substring(path.lastIndexOf(".") + 1, path.length());
        try {
            InputStream stream = new FileInputStream(path);
            //如果后缀名为xls，使用HSSF
            if (fileType.equals("xls")) {
                workbook = new HSSFWorkbook(stream);
                //如果后缀名是xlsx，使用XSSF
            }else if (fileType.equals("xlsx")){
                workbook = new XSSFWorkbook(stream);

            }
            Sheet sheet= workbook.getSheet("sheet1");
            //获取行数
            int rows=sheet.getPhysicalNumberOfRows();
            List<STable> sTables=new ArrayList<>();
            for(int currentRow=0;currentRow<rows;currentRow++){
                STable s=new STable();//表格的每一行为一个实例；
                DataFormatter formatter = new DataFormatter();
                String studentId = formatter.formatCellValue(sheet.getRow(currentRow).getCell(0));
                String name = sheet.getRow(currentRow).getCell(1).getStringCellValue();
                String classInfo = sheet.getRow(currentRow).getCell(2).getStringCellValue();
                s.setStudentId(studentId);
                s.setName(name);
                s.setClassInfo(classInfo);
                sTables.add(s);
            }
            //String teacherId=(String)request.getSession().getAttribute("teacherId");
            System.out.println("teacherId"+teacherId);
            studentService.importStudent(sTables,teacherId);

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 教师文件上传导入 连续化
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/doImportTeacherExcel")
    public Map<String, Object> TeacherUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        String fileName = "";
        String path = "";
        try {
            //将当前上下文初始化给  CommonsMutipartResolver
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //检查form中是否有enctype="multipart/form-data"
            if (multipartResolver.isMultipart(request)) {
                //将request变成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                //获取multiRequest 中所有的文件名
                Iterator iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    //一次遍历所有文件
                    MultipartFile file1 = multiRequest.getFile(iter.next().toString());
                    if (file != null) {
                        //获取上传文件名
                        fileName = file1.getOriginalFilename();
                        //获取后缀名
                        String sname = fileName.substring(fileName.lastIndexOf("."));
                        //时间格式化格式
                        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMddHHmmssSSS");
                        //获取当前时间并作为时间戳
                        String timeStamp=simpleDateFormat.format(new Date());
                        //拼接新的文件名
                        String newName ="教师表"+timeStamp+sname;//命名+时间戳+后缀
                        //指定上传文件的路径
                        //"C:/Users/11139/examsystem/"
                        path = "S:/sqlTest/examsystem/" + newName;
                        //上传保存
                        /* file.transferTo();*/
                        FileUtils.copyInputStreamToFile(file.getInputStream(),new File("S:/sqlTest/examsystem/",newName));
                        ////保存当前文件路径
                        //request.getSession().setAttribute("currFilePath", path);
                    }
                }
            }

            result.put("statusCode", "200");
            result.put("status",1);
            result.put("message", "上传成功!");
            result.put("filename", fileName);
        } catch (Exception ex) {
            result.put("statusCode", "300");
            result.put("status",0);
            result.put("message", "上传失败:" + ex.getMessage());
        }

        parseTeacherExcel(request,path);

        return result;
    }

    /**
     * 已同导入教师文件整合，不推荐单独使用---导入题目文件到数据库
     *      对比之前增加了path参数 为表格文件路径
     * @param request
     * @param path      表格文件路径
     * @return
     */
    @ResponseBody
    @RequestMapping("/parseTeacherExcel")
    public Map<String, Object> parseTeacherExcel(HttpServletRequest request, String path) {
        Map<String, Object> result = new HashMap<String, Object>();
        Workbook workbook = null;
        ////获取文件路径
        //path =(String)request.getSession().getAttribute("currFilePath");
        System.out.println("测试路径"+path);//测试看看路径正确？
        //获取文件格式
        String fileType = path.substring(path.lastIndexOf(".") + 1, path.length());
        try {
            InputStream stream = new FileInputStream(path);
            //如果后缀名为xls，使用HSSF
            if (fileType.equals("xls")) {
                workbook = new HSSFWorkbook(stream);
                //如果后缀名是xlsx，使用XSSF
            }else if (fileType.equals("xlsx")){
                workbook = new XSSFWorkbook(stream);

            }
            Sheet sheet= workbook.getSheet("sheet1");
            //获取行数
            int rows=sheet.getPhysicalNumberOfRows();
            List<TTable> tTables=new ArrayList<>();
            for(int currentRow=0;currentRow<rows;currentRow++){
                System.out.println(currentRow);
                TTable t=new TTable();//表格的每一行为一个实例；
                DataFormatter formatter = new DataFormatter();
                String teacherId = formatter.formatCellValue(sheet.getRow(currentRow).getCell(0));
                String name = sheet.getRow(currentRow).getCell(1).getStringCellValue();
                t.setTeacherId(teacherId);
                t.setTeacherName(name);
                tTables.add(t);
            }
            teacherService.importTeacher(tTables);


        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 上传题目表 整合 导入数据库(用parse)
     * @version 2.0
     * @param request
     * @param file
     * @return
     * @author lw
     * @date 2020年8月22日 10:38
     */
    @ResponseBody
    @RequestMapping("/doImportQuestionExcel")
    public Map<String, Object> QuestionUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        String fileName = "";
        String path = "";
        try {
            //将当前上下文初始化给  CommonsMutipartResolver
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //检查form中是否有enctype="multipart/form-data"
            if (multipartResolver.isMultipart(request)) {
                //将request变成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                //获取multiRequest 中所有的文件名
                Iterator iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    //一次遍历所有文件
                    MultipartFile file1 = multiRequest.getFile(iter.next().toString());
                    if (file != null) {
                        //获取上传文件名
                        fileName = file1.getOriginalFilename();
                        //获取后缀名
                        String sname = fileName.substring(fileName.lastIndexOf("."));
                        //时间格式化格式
                        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMddHHmmssSSS");
                        //获取当前时间并作为时间戳
                        String timeStamp=simpleDateFormat.format(new Date());
                        //拼接新的文件名
                        String newName ="题目表"+timeStamp+sname;//命名+时间戳+后缀
                        //指定上传文件的路径  C:/Users/11139/examsystem/
                        // S:/sqlTest/examsystem/
                        path = "S:/sqlTest/examsystem/" + newName;
                        //上传保存
                        /* file.transferTo(); C:/Users/11139/examsystem/ */
                        FileUtils.copyInputStreamToFile(file.getInputStream(),new File("S:/sqlTest/examsystem/",newName));
                        ////保存当前文件路径
                        //request.getSession().setAttribute("currFilePath", path);
                    }
                }
            }

            result.put("statusCode", "200");
            result.put("status",1);
            result.put("message", "上传成功!");
            result.put("filename", fileName);
        } catch (Exception ex) {
            result.put("statusCode", "300");
            result.put("status",0);
            result.put("message", "上传失败:" + ex.getMessage());
        }

        //////以下为parse部分///////
        parseQuestionExcel(request,path);

        return result;
    }

    /**
     * 已同导入文件整合，不推荐单独使用---导入题目文件到数据库
     *      对比之前增加了path参数 为表格文件路径
     * @version 2.0
     * @param request
     * @param path 文件存储路径
     * @return
     * @author lw
     * @date 2020年8月22日 10:40
     */
    @ResponseBody
    @RequestMapping("/parseQuestionExcel")
    public Map<String, Object> parseQuestionExcel(HttpServletRequest request,String path) {
        Map<String, Object> result = new HashMap<String, Object>();
        Workbook workbook = null;
        //获取文件路径
        //String path =(String)request.getSession().getAttribute("currFilePath");
        System.out.println("测试路径"+path);//测试看看路径正确？
        //获取文件格式
        String fileType = path.substring(path.lastIndexOf(".") + 1, path.length());
        try {
            InputStream stream = new FileInputStream(path);
            //如果后缀名为xls，使用HSSF
            if (fileType.equals("xls")) {
                workbook = new HSSFWorkbook(stream);
                //如果后缀名是xlsx，使用XSSF
            }else if (fileType.equals("xlsx")){
                workbook = new XSSFWorkbook(stream);

            }
            Sheet sheet= workbook.getSheet("sheet1");
            //获取行数
            int rows=sheet.getPhysicalNumberOfRows();
            List<QTable> qTables=new ArrayList<>();
            for(int currentRow=0;currentRow<rows;currentRow++){
                QTable q = new QTable();
                DataFormatter formatter = new DataFormatter();
                //Integer currentNum = new Integer(currentRow);
                //String id = currentNum.toString();      //自增的id 可能和设置的产生冲突 ？
                String chapter = sheet.getRow(currentRow).getCell(0).getStringCellValue();
                String type = sheet.getRow(currentRow).getCell(1).getStringCellValue();
                String title = sheet.getRow((currentRow)).getCell(2).getStringCellValue();
                String option1 = formatter.formatCellValue(sheet.getRow(currentRow).getCell(3));
                String option2 = formatter.formatCellValue(sheet.getRow(currentRow).getCell(4));
                String option3 = formatter.formatCellValue(sheet.getRow(currentRow).getCell(5));
                String option4 = formatter.formatCellValue(sheet.getRow(currentRow).getCell(6));
                String answer = sheet.getRow(currentRow).getCell(9).getStringCellValue();
                answer = deleteCharInStr(answer,new char[]{'E','F'});   //在答案中删除 E F
                q.setChapter(chapter);
                q.setTitle(title);
                q.setType(type);
                q.setOption1(option1);
                q.setOption2(option2);
                q.setOption3(option3);
                q.setOption4(option4);
                q.setAnswer(answer);
                qTables.add(q);
            }
            adminService.importQuestion(qTables);

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除str中的某些字符
     * @param str   操作字符串
     * @param chars 要删去的字符数组
     * @return
     */
    public static String deleteCharInStr(String str, char[] chars){
        String delStr = "";
        int flag = 1;
        for (int i = 0; i < str.length(); i++) {
            flag = 1;
            for (int j = 0; j < chars.length; j++) {
                if(str.charAt(i) == chars[j]){
                    //如果出现相同字符，则置位，删除
                    flag = 0;
                }
            }
            if(flag==1){
                delStr += str.charAt(i);
            }
        }
        return  delStr;
    }
}
