@echo off
title=�������Դ��Ӧ��

mshta vbscript:msgbox("ע�ⴰ�����ã�����->ѡ��->ȡ�������ٱ༭ģʽ������ֹ������ͣ����",1,"��ʾ")(window.close)

title=�������Դ��Ӧ��
color 0b

set install_path=%~dp0
cd /D %install_path%

rem �رն˿�ռ��--
rem for /f "tokens=5" %%i in ('netstat -aon ^| findstr ":9527"') do (
rem set n=%%i
rem )
rem if defined n (taskkill /f /pid %n%)
rem �رն˿�ռ��--
 
 
java   -Dfile.encoding=GBK -jar my_send-2.1.jar

rem @pause




