@echo off

REM JavaFX Script execution script for Linux/Solaris/OS X.
REM
REM Uses the same arguments as the JDK's java command.

REM %~dp0 is expanded pathname of the current script
set _JAVAFX_LIBS=%~dp0..\lib

if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=%JAVA_HOME%\bin\java.exe
goto setArguments

:noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=java.exe

:setArguments
set _CP_=%CLASSPATH%
if "%_CP_%" == "" set _CP_=.
set _ARGS=

:jvmoptsLoop
if "%1" == "" goto jvmoptsDone
if "%1" == "-cp" goto getClasspath
if "%1" == "-classpath" goto getClasspath
goto fxarg

:getClasspath
shift
set _CP_=%~1%

goto jvmoptsNext

:fxarg
set _FX_ARGS=%_FX_ARGS% %1%
goto jvmoptsNext

:jvmoptsNext
shift
goto jvmoptsLoop

:jvmoptsDone
set _CLASSPATH=%_JAVAFX_LIBS%\javafxrt.jar;%_JAVAFXC_HOME%\javafxgui.jar;%_JAVAFXC_HOME%\javafx-swing.jar;%_JAVAFX_LIBS%\@SCENEGRAPH_JAR@;%_JAVAFX_LIBS%\Decora-HW.jar;%_JAVAFX_LIBS%\Decora-D3D.jar;%_JAVAFX_LIBS%\jmc.jar;%_CP_%
set _CP_=
set _VAL=
set _CMP=

"%_JAVACMD%" -Djava.library.path="%_JAVAFX_LIBS%" -classpath "%_CLASSPATH%" %_FX_ARGS%

REM cleanup
set _CLASSPATH=
if not "%_JAVAFX_LIBS"=="" set _JAVAFX_LIBS=
if not "%_JAVACMD%"=="" set _JAVACMD=
if not "%_JVM_ARGS%"=="" set _JVM_ARGS=
if not "%_FX_ARGS%"=="" set _FX_ARGS=
if not "%_ARGS%"=="" set _ARGS=
