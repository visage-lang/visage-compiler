@echo off

if "%VISAGE_HOME%"=="" goto noJavafxHome

if not exist "%BTRACE_HOME%\build\btrace-agent.jar" goto noBTraceHome

%VISAGE_HOME%\bin\visage -javaagent:%BTRACE_HOME%\build\btrace-agent.jar=dumpClasses=false,debug=false,unsafe=true,probeDescPath=.,noServer=true,script=%1 %2 %3 %4 %5 %6 %7 %8 %9
goto end

:noBTraceHome
  echo Please set BTRACE_HOME before running this script
  goto end

:noJavafxHome
  echo Please set VISAGE_HOME before running this script
:end
