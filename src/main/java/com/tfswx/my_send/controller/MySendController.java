package com.tfswx.my_send.controller;

import com.tfswx.my_send.utils.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/mySend")
public class MySendController {
    private static Logger log = LoggerFactory.getLogger(MySendController.class);

    //文件存放地址
    public static String wsPath = "";//"N:\\";
    public static String dzjzPath = "";//"Z:\\JdNewData\\";

    //文件类型
    private static final String WS_TYPE = "w";
    private static final String DZJZ_TYPE = "d";

    private static int count;
    /**
     * 获取文件byte[]并反馈
     *
     * @param fileName
     * @param fileType
     * @return
     */
    @RequestMapping("/getFile")
    @ResponseBody
    public byte[] getFile(String fileName, String fileType) {

        count++;
        if(count<34567 && count%239==0){//只是便于查看窗口是否正常运行，具体信息看日志文件
            System.out.print(".");
        }else{
            if (count>=34567) {count=0;
                System.out.println(".");}
        }
        File file;
        switch (fileType) {
            case WS_TYPE:
                file = new File(wsPath + fileName);
                if (file.exists()) {
                    log.info("文书存在并返回："+fileName);
                    return getBytes(file);
                } else {
                    log.info("文书不存在："+fileName);
                    return new byte[0];
                }
            case DZJZ_TYPE:
                file = new File(dzjzPath + fileName);
                if (file.exists()) {
                    log.info("卷宗存在并返回："+fileName);
                    return getBytes(file);
                } else {
                    log.info("卷宗不存在："+fileName);
                    return new byte[0];
                }
        }
        return new byte[0];
    }

    @RequestMapping("/getPath")
    @ResponseBody
    public String getPath() {
        return "文书地址：" + wsPath + "；电子卷宗地址：" + dzjzPath;
    }


    @RequestMapping("/getWsPath")
    @ResponseBody
    public String getWsPath(String path) {
        log.warn("获取文书路径");
        return wsPath;
    }

    @RequestMapping("/getDzjzPath")
    @ResponseBody
    public String getDzjzPath(String path) {
        log.warn("获取卷宗路径");
        return dzjzPath;
    }

    @RequestMapping("/setWsPath")
    @ResponseBody
    public JsonResult setWsPath(String path) {
        log.warn("响应文书路径配置");
        File myfile = new File(path);
        boolean isUpdate = false;
        String test = "^\\d{6}$";
        if (myfile.exists() && myfile.isDirectory()) {
            File[] files = myfile.listFiles();
            for (File file : files) {
                if (file.isDirectory() && file.getName().matches(test)) {
                    Boolean judge = judgeFile(file);
                    if (judge != null && !judge) {
                        isUpdate = true;
                    }
                }
            }
        }
        if (isUpdate) {
            if(myfile.getAbsolutePath().split(":")[1].length()>1){
                wsPath = getPathByPath(myfile.getAbsolutePath())+"\\";
            }else {
                wsPath = myfile.getAbsolutePath();
            }
            return new JsonResult(wsPath);
        } else {
            return new JsonResult(1, "文书地址修改失败，未检测到文书文件");
        }


    }


    @RequestMapping("/setDzjzPath")
    @ResponseBody
    public JsonResult setDzjzPath(String path) {
        log.warn("响应电子卷宗路径配置");
        File myfile = new File(path);
        boolean isUpdate = false;
        String test = "^\\d{6}$";
        if (myfile.exists() && myfile.isDirectory()) {
            File[] files = myfile.listFiles();
            for (File file : files) {
                if (file.isDirectory() && file.getName().matches(test)) {
                    Boolean judge = judgeFile(file);
                    if (judge != null && judge) {
                        isUpdate = true;
                    }
                }
            }
        }
        if (isUpdate) {
            dzjzPath = getPathByPath(myfile.getAbsolutePath());
            return new JsonResult(dzjzPath);
        } else {
            return new JsonResult(1, "电子卷宗地址修改失败,未检测到电子卷宗文件");
        }

    }



    //判断文件以什么结尾来判断该目录存放的什么文件
    public static Boolean judgeFile(File myfile) {
        File[] files = myfile.listFiles();
        for (File file : files) {
//            log.info(file.getName());
            if (file.isDirectory()) {
                Boolean judge = judgeFile(file);
                if (judge != null) {
                    return judge;
                }
            } else {
//                log.info(file.getName().indexOf(".encry"));
                if (file.getName().indexOf(".encry") != -1) {
                    if (file.getName().indexOf(".pdf") != -1) {
                        return true;
                    }
                    if (file.getName().indexOf(".doc") != -1) {
                        return false;
                    }
                }
            }

        }
        return null;
    }


    private String getPathByPath(String path) {
        if (path.endsWith("\\") || path.endsWith("/")) {
            return getPathByPath(path.substring(0, path.length() - 1));
        }
        return path;
    }

    /**
     * 将文件转为byte[]
     *
     * @param file
     * @return
     */
    private byte[] getBytes(File file) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        byte[] b = new byte[4096];
        int n = -1;
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream(4096);
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }

}
