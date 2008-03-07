@echo off

REM JavaFX Script documentation generation script for Windows.
REM
REM Uses the same core arguments as the JDK's javadoc command, but uses 
REM the XMLDoclet and an XSLT translator to generate documentation.  Run
REM javafxdoc without arguments to see the XMLDoclet options.

REM %~dp0 is expanded pathname of the current script
set _JAVAFXDOC_HOME=%~dp0..\lib

if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=%JAVA_HOME%\bin\java.exe
goto setArguments

:noJavaHome
if "%_JAVACMD%" == "" set _JAVACMD=java.exe

if "%1" == "" goto printhelp

:setArguments
if "%CLASSPATH%" == "" set _CP_=.
if not "%CLASSPATH%" == "" set _CP_=.;%CLASSPATH%
set _ARGS=
set _JVM_ARGS=

:jvmoptsLoop
if "%1" == "" goto jvmoptsDone
if "%1" == "-cp" goto getClasspath
if "%1" == "-classpath" goto getClasspath
if "%1:~0,2%" == "-J" goto jvmarg
goto fxarg

:getClasspath
shift
set _CP_=%~1%

goto jvmoptsNext

:fxarg
set _FX_ARGS=%_FX_ARGS% %1%
goto jvmoptsNext

:jvmarg
set _JVM_ARGS=%_JVM_ARGS% %1:~2%

:jvmoptsNext
shift
goto jvmoptsLoop

:jvmoptsDone
set _CLASSPATH=%_CP_%
set _CP_=
set _VAL=
set _CMP=

"%_JAVACMD%" %_JVM_ARGS% "-Xbootclasspath/p:%_JAVAFXDOC_HOME%\javafxc.jar;%_JAVAFXDOC_HOME%\javafxdoc.jar" com.sun.tools.javafxdoc.Main -classpath "%_CLASSPATH%" %_FX_ARGS%
goto cleanup

:printhelp
"%_JAVACMD%" "-Xbootclasspath/p:%_JAVAFXDOC_HOME%\javafxc.jar;%_JAVAFXDOC_HOME%\javafxdoc.jar" com.sun.tools.javafxdoc.Main -help

:cleanup
if not "%_JAVAFXDOC_HOME"=="" set _JAVAFXDOC_HOME=
if not "%_JAVACMD%"=="" set _JAVACMD=
if not "%_JVM_ARGS%"=="" set _JVM_ARGS=
if not "%_FX_ARGS%"=="" set _FX_ARGS=
if not "%_CLASSPATH"=="" set _CLASSPATH=