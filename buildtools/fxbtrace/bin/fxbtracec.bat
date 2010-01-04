@echo off

if "%JAVAFX_HOME%"=="" goto noJavafxHome

if not exist "%BTRACE_HOME%\build\btrace-agent.jar" goto noBTraceHome

javac -cp "%BTRACE_HOME%\build\btrace-client.jar;%JAVAFX_HOME%\lib\shared\javafxrt.jar" %*
goto end

:noBTraceHome
  echo Please set BTRACE_HOME before running this script
  goto end

:noJavafxHome
  echo Please set JAVAFX_HOME before running this script
:end
