package com.kenji.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileUploadUtil {

    public static Map<String,String> uploadFile(HttpServletRequest request, String[] suffixes, long limitSize, int fileNum) throws IOException, FileUploadException, SuffixNotMatchException, OutOfLimitSizeException, OutOfLimitFileNumException {
        //创建一个工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);

        if(!ServletFileUpload.isMultipartContent(request)){//fileUpLoad工具包只能解析Multipart结构的，所以先进行判断
            return null;
        }

        Map<String,String> params = new HashMap<>();//将参数和值存入
            //将data中part部分解析 FileItem
        List<FileItem> fileItemList = servletFileUpload.parseRequest(request);//将request中在multipart的参数解析成fileItem
        for(FileItem fileItem : fileItemList){
            //这种表单发出来只有两种可能 正常字段 二进制(文件)
            if(fileItem.isFormField()){//表示是表单字段
                params.put(fileItem.getFieldName(),new String(fileItem.getString().getBytes("ISO-8859-1"),"UTF-8"));
//                    System.out.println(fileItem.getFieldName() + "=" + new String(fileItem.getString().getBytes("ISO-8859-1"),"UTF-8"));//获取字段的名和值
                //需要将字符串先以ISO格式变为bytes数组 再创建一个新的字符串以UTF-8
            }else {//二进制文件
                String fieldName = fileItem.getFieldName();
                String jsonStr = params.get(fieldName);//fieldName为字段名
                JSONArray jsonArray = null;
                if(jsonStr == null){//当前上传的文件，是这个Field的第一个文件 为保证相同字段名的文件在一个jsonArray中存储
                    jsonArray = new JSONArray();
                }else {//当前上传的文件不是当前的这个Field的第一个文件
                    jsonArray = JSON.parseArray(jsonStr);//将当前的json字符串转化为json数组
                    if(jsonArray.size() >= fileNum){
                        throw new OutOfLimitFileNumException();
                    }
                }
                String originFileName = fileItem.getName();//获取上传的文件名
                if(!suffixIsOK(originFileName,suffixes)) throw new SuffixNotMatchException();
                if(fileItem.getSize() > limitSize) throw new OutOfLimitSizeException();//文件大小限定
                String filePath = "F:/upload_files/";//定义一个文件存储的路径
                String realFileName = filePath + UUID.randomUUID().toString();//获取到一个随机数为要保存在硬盘上的文件名
                File file = new File(realFileName);//创建文件对象
                file.createNewFile();//在磁盘上创建文件，此时文件是一个空文件
                //文件在网络中，我们获取到的都是二进制流
                InputStream inputStream = fileItem.getInputStream();//获取上传文件的流
                FileOutputStream fileOutputStream = new FileOutputStream(file);//获取写入文件的流
                IOUtils.copy(inputStream,fileOutputStream);//将input流中的东西读出来放入另一个流中
                inputStream.close();
                fileOutputStream.close();
                //需要知道原文件名和路径
                //构建文件信息的JSON 我们需要保存文件的原名和当前的路径全名
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("originFileName",originFileName);//原文件名
                jsonObject.put("realFileName",realFileName);//现文件名和路径
                jsonArray.add(jsonObject);//将这个json对象添加到json数组
                params.put(fieldName,jsonArray.toJSONString());//将这个json数组写回到map
            }
        }
        return params;
    }


    private static boolean suffixIsOK(String fileName,String[] suffixes){
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        for(String temp : suffixes){
            if(temp.equals(suffix.toLowerCase())){//将文件后缀全部转为小写再进行判断
                return true;
            }
        }
        return false;
    }

    public static class SuffixNotMatchException extends Exception{
        public SuffixNotMatchException(){
            super();
        }
        public SuffixNotMatchException(String msg){
            super(msg);
        }
    }

    public static class OutOfLimitSizeException extends Exception{
        public OutOfLimitSizeException(){
            super();
        }
        public OutOfLimitSizeException(String msg){
            super(msg);
        }
    }

    public static class OutOfLimitFileNumException extends Exception{
        public OutOfLimitFileNumException(){
            super();
        }
        public OutOfLimitFileNumException(String msg){
            super(msg);
        }
    }

}
