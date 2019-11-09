@echo off
title=文书卷宗源端应用
rem mode con cols=80 lines=30&
color 02


rem LANG=zh_CN
rem 获取当前目录路径
set install_path=%~dp0
cd /D %install_path%
 
java   -Dfile.encoding=GBK -jar E:\SJFH2\my_send-2.1.jar

