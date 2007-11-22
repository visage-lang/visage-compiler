@echo off

REM JavaFX Script execution script for Linux/Solaris/OS X.
REM
REM Uses the same arguments as the JDK's java command.

REM %~dp0 is expanded pathname of the current script
set _JAVAFXC_HOME=%~dp0..

if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=%JAVA_HOME%\bin\java.exe
goto setArguments

:noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=java.exe

:setArguments
set _JVM_ARGS=
set _ARGS=%*
if not defined _ARGS goto jvmoptsDone
set _ARGS=%_ARGS:-=-d%
set _ARGS=%_ARGS:"=-q%
set _ARGS="%_ARGS%"

:jvmoptsLoop
for /f "tokens=1,*" %%i in (%_ARGS%) do call :getarg "%%i" "%%j"
goto processarg

:getarg
for %%i in (%1) do set _CMP=%%~i
set _ARGS=%2
goto :EOF

:processarg
if "%_CMP%" == "" goto jvmoptsDone

set _CMP=%_CMP:-q="%
set _CMP=%_CMP:-d=-%
if "%_CMP:~0,2%" == "-J" goto jvmarg

:fxarg
set _FX_ARGS=%_FX_ARGS% %_CMP%
goto jvmoptsNext

:jvmarg
set _VAL=%_CMP:~2%
set _JVM_ARGS=%_JVM_ARGS% %_VAL%

:jvmoptsNext
set _CMP=
goto jvmoptsLoop

:jvmoptsDone
set _VAL=
set _CMP=

"%_JAVACMD%" %_JVM_ARGS% "-Xbootclasspath/p:%_JAVAFXC_HOME%\javafxc.jar;%_JAVAFXC_HOME%\javafxrt.jar" %_FX_ARGS%

REM cleanup
if not "%_JAVAFXC_HOME"=="" set _JAVAFXC_HOME=
if not "%_JAVACMD%"=="" set _JAVACMD=
if not "%_JVM_ARGS%"=="" set _JVM_ARGS=
if not "%_FX_ARGS%"=="" set _FX_ARGS=
