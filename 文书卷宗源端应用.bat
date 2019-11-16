@echo off
title=文书卷宗源端应用

mshta vbscript:msgbox("注意窗口设置：属性->选项->取消【快速编辑模式】，防止单击暂停程序",1,"提示")(window.close)

title=文书卷宗源端应用
color 0b

set install_path=%~dp0
cd /D %install_path%

rem 关闭端口占用--
rem for /f "tokens=5" %%i in ('netstat -aon ^| findstr ":9527"') do (
rem set n=%%i
rem )
rem if defined n (taskkill /f /pid %n%)
rem 关闭端口占用--
 
 
java   -Dfile.encoding=GBK -jar my_send-2.1.jar

rem @pause




