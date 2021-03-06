@echo off

if "%VISAGE_HOME%"=="" goto noVisageHome

if not exist "%BTRACE_HOME%\build\btrace-agent.jar" goto noBTraceHome

javac -cp "%BTRACE_HOME%\build\btrace-client.jar;%VISAGE_HOME%\lib\shared\visagert.jar" %*
goto end

:noBTraceHome
  echo Please set BTRACE_HOME before running this script
  goto end

:noVisageHome
  echo Please set VISAGE_HOME before running this script
:end
