package com.tfswx.my_send;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KillServerTest {
    private Set<Integer> ports;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("请输入要杀掉的windows进程的端口号，如果有多个，则以逗号相隔");
        System.out.println("Please input kill port");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        scanner.close();
        String[] split = input.split(",");
        Set<Integer> ports = new HashSet<>();
        for (String spid : split) {
            try {
                int pid = Integer.parseInt(spid);
                ports.add(pid);
            } catch (Exception e) {
                System.out.println("错误的端口号，请输入一个或者多个端口，以英文逗号隔开");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
//                System.exit(0);
            }
        }

        KillServerTest kill = new KillServerTest();
        kill.ports = ports;
        System.out.println("need kill " + ports.size() + " num");
        for (Integer pid : ports) {
            kill.start(pid);
        }
        System.out.println("清理完毕");
        System.out.println("SUCCESS");
        Thread.sleep(1000);
//        System.exit(0);

    }

    public void start(int port) {
        Runtime runtime = Runtime.getRuntime();
        try {
            //查找进程号
            Process p = runtime.exec("cmd /c netstat -ano | findstr \"" + port + "\"");
            InputStream inputStream = p.getInputStream();
            List<String> read = read(inputStream, "UTF-8");
            if (read.size() == 0) {
                System.out.println("找不到该端口的进程");
                try {
                    Thread.sleep(6000);
                    System.exit(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                for (String string : read) {
                    System.out.println(string);
                }
                System.out.println("找到" + read.size() + "个进程，正在准备清理");
                kill(read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证此行是否为指定的端口，因为 findstr命令会是把包含的找出来，例如查找80端口，但是会把8099查找出来
     *
     * @param str
     * @return
     */
    private boolean validPort(String str) {
        Pattern pattern = Pattern.compile("^ *[a-zA-Z]+ +\\S+");
        Matcher matcher = pattern.matcher(str);

        matcher.find();
        String find = matcher.group();
        int spstart = find.lastIndexOf(":");
        find = find.substring(spstart + 1);

        int port = 0;
        try {
            port = Integer.parseInt(find);
        } catch (NumberFormatException e) {
            System.out.println("查找到错误的端口:" + find);
            return false;
        }
        if (this.ports.contains(port)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更换为一个Set，去掉重复的pid值
     *
     * @param data
     */
    public void kill(List<String> data) {
        Set<Integer> pids = new HashSet<>();
        for (String line : data) {
            int offset = line.lastIndexOf(" ");
            String spid = line.substring(offset);
            spid = spid.replaceAll(" ", "");
            int pid = 0;
            try {
                pid = Integer.parseInt(spid);
            } catch (NumberFormatException e) {
                System.out.println("获取的进程号错误:" + spid);
            }
            pids.add(pid);
        }
        killWithPid(pids);
    }

    /**
     * 一次性杀除所有的端口
     *
     * @param pids
     */
    public void killWithPid(Set<Integer> pids) {
        for (Integer pid : pids) {
            try {
                Process process = Runtime.getRuntime().exec("taskkill /F /pid " + pid + "");
                InputStream inputStream = process.getInputStream();
                String txt = readTxt(inputStream, "GBK");
                System.out.println(txt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private List<String> read(InputStream in, String charset) throws IOException {
        List<String> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        String line;
        while ((line = reader.readLine()) != null) {
            boolean validPort = validPort(line);
            if (validPort) {
                data.add(line);
            }
        }
        reader.close();
        return data;
    }

    public String readTxt(InputStream in, String charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}