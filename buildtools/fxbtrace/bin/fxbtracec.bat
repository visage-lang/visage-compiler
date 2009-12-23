@echo off

rem %~dp0 is expanded pathname of the current script under NT
set DEFAULT_BTRACE_HOME=%~dp0..

if "%BTRACE_HOME%"=="" set BTRACE_HOME=%DEFAULT_BTRACE_HOME%
set DEFAULT_BTRACE_HOME=

if "%JAVAFX_HOME%"=="" goto noJavafxHome

if not exist "%BTRACE_HOME%\build\btrace-agent.jar" goto noBTraceHome

javac -cp "%BTRACE_HOME%/build/btrace-client.jar;%JAVAFX_HOME%/lib/shared/javafxrt.jar" %*
goto end

:noBTraceHome
  echo Please set BTRACE_HOME before running this script
:end

:noJavafxHome
  echo Please set JAVAFX_HOME before running this script
:end
