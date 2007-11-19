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
set _CMD_LINE_ARGS=%1
if ""%1""=="""" goto runCompiler
shift
:setupArgs
if ""%1""=="""" goto runCompiler
set _CMD_LINE_ARGS=%_CMD_LINE_ARGS% %1
shift
goto setupArgs

:runCompiler
"%_JAVACMD%" "-Xbootclasspath/p:%_JAVAFXC_HOME%\javafxc.jar;%_JAVAFXC_HOME%\javafxrt.jar" %_CMD_LINE_ARGS%

REM cleanup
if not "%_JAVAFXC_HOME"=="" set _JAVAFXC_HOME=
if not "%_JAVACMD%"=="" set _JAVACMD=
if not "%_CMD_LINE_ARGS%"=="" set ANT_CMD_LINE_ARGS=
