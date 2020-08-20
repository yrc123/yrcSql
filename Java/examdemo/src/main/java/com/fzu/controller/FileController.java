package com.fzu.controller;
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

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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


import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class FileController {

    @Autowired
    StudentService studentService=new StudentServiceImpl();
    @Autowired
    AdminService adminService=new AdminServiceImpl();
    TeacherService teacherService=new TeacherServiceImpl();


    @ResponseBody
    @RequestMapping("/doImportStudentExcel")
    public Map<String, Object> StudentUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file,
                                        @RequestParam("teacherId") String  teacherId) {
        Map<String, Object> result = new HashMap<String, Object>();
        String fileName = "";
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
                        String path = "C:/Users/11139/examsystem/" + newName;
                        //上传保存
                       /* file.transferTo();*/
                        FileUtils.copyInputStreamToFile(file.getInputStream(),new File("C:/Users/11139/examsystem/",newName));
                        //保存当前文件路径
                        request.getSession().setAttribute("currFilePath", path);
                        request.getSession().setAttribute("teacherId", teacherId);
                    }
                }
            }

            result.put("statusCode", "200");
            result.put("message", "上传成功!");
            result.put("filename", fileName);
        } catch (Exception ex) {
            result.put("statusCode", "300");
            result.put("message", "上传失败:" + ex.getMessage());
        }
        return result;
    }


    @ResponseBody
    @RequestMapping("/parseStudentExcel")
    public Map<String, Object> parseStudentExcel(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        Workbook workbook = null;
        //获取文件路径
        String path =(String)request.getSession().getAttribute("currFilePath");
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
            String teacherId=(String)request.getSession().getAttribute("teacherId");
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

    @ResponseBody
    @RequestMapping("/doImportTeacherExcel")
    public Map<String, Object> TeacherUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        String fileName = "";
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
                        String path = "C:/Users/11139/examsystem/" + newName;
                        //上传保存
                        /* file.transferTo();*/
                        FileUtils.copyInputStreamToFile(file.getInputStream(),new File("C:/Users/11139/examsystem/",newName));
                        //保存当前文件路径
                        request.getSession().setAttribute("currFilePath", path);
                    }
                }
            }

            result.put("statusCode", "200");
            result.put("message", "上传成功!");
            result.put("filename", fileName);
        } catch (Exception ex) {
            result.put("statusCode", "300");
            result.put("message", "上传失败:" + ex.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/parseTeacherExcel")
    public Map<String, Object> parseTeacherExcel(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        Workbook workbook = null;
        //获取文件路径
        String path =(String)request.getSession().getAttribute("currFilePath");
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
                TTable t=new TTable();//表格的每一行为一个实例；
                String teacherId = sheet.getRow(currentRow).getCell(0).getStringCellValue();
                String name = sheet.getRow(currentRow).getCell(1).getStringCellValue();
                t.setTeacherId(teacherId);
                t.setName(name);
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

}
