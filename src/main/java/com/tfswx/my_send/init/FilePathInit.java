package com.tfswx.my_send.init;

import com.tfswx.my_send.controller.MySendController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FilePathInit implements CommandLineRunner {
    @Value("${path.ws-path}")
    private  String cfg_wsPath ;
    @Value("${path.dzjz-path}")
    private  String cfg_dzjzPath ;

    @Override
    public void run(String... args){
        MySendController.wsPath=cfg_wsPath;
        MySendController.dzjzPath=cfg_dzjzPath;

       getFilePath();

        System.out.println("文书目录：" + MySendController.wsPath);
        System.out.println("电子卷宗目录：" + MySendController.dzjzPath);
    }

    /**
     * 获取电子卷宗和文书的根目录
     */
    public void getFilePath() {
        //文件是否被找到
        boolean dzjzPathIsFind = false;
        boolean wsPathIsFind = false;
        //检测单位文件
        String test = "^\\d{6}$";
        //获取磁盘目录
        File[] roots = File.listRoots();
        //遍历磁盘目录验证文件是否是我需要的（后续可以递归遍历，目前只遍历了一层）
        for (int i = roots.length - 1; i > 0; i--) {
            if (roots[i].isDirectory()) {
                File[] files = roots[i].listFiles();
                for (File file : files) {
//                    System.out.println(file.getName());
                    if (file.isDirectory() && file.getName().matches(test)) {
                        Boolean judge = judgeFile(file);
                        if (judge != null) {
                            if (judge) {
                                MySendController.dzjzPath = roots[i].getAbsolutePath().substring(0,2);
                                dzjzPathIsFind = true;
                            } else {
                                MySendController.wsPath = roots[i].getAbsolutePath();
                                wsPathIsFind = true;
                            }
                        }
                    }
                    //当文件都找到了，就跳出循环
                    if (dzjzPathIsFind && wsPathIsFind) {
                        break;
                    }
                }
            }
        }

    }

    //判断文件以什么结尾来判断该目录存放的什么文件
    public static Boolean judgeFile(File myfile) {
        File[] files = myfile.listFiles();
        for (File file : files) {
//            System.out.println(file.getName());
            if (file.isDirectory()) {
                Boolean judge = judgeFile(file);
                if (judge != null) {
                    return judge;
                }
            } else {
//                System.out.println(file.getName().indexOf(".encry"));
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



}
