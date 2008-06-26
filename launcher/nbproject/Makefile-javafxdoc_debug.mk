#
# Gererated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc.exe
CCC=g++.exe
CXX=g++.exe
FC=

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/javafxdoc_debug

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/src/configuration.o \
	${OBJECTDIR}/src/javafxc.o \
	${OBJECTDIR}/src/javafxdoc.o \
	${OBJECTDIR}/src/javafx.o

# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=-mno-cygwin
CXXFLAGS=-mno-cygwin

# Fortran Compiler Flags
FFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS} dist/Debug/javafxdoc.exe

dist/Debug/javafxdoc.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Debug
	${LINK.cc} -o dist/Debug/javafxdoc ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/src/configuration.o: src/configuration.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFXDOC -o ${OBJECTDIR}/src/configuration.o src/configuration.cpp

${OBJECTDIR}/src/javafxc.o: src/javafxc.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFXDOC -o ${OBJECTDIR}/src/javafxc.o src/javafxc.cpp

${OBJECTDIR}/src/javafxdoc.o: src/javafxdoc.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFXDOC -o ${OBJECTDIR}/src/javafxdoc.o src/javafxdoc.cpp

${OBJECTDIR}/src/javafx.o: src/javafx.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFXDOC -o ${OBJECTDIR}/src/javafx.o src/javafx.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf:
	${RM} -r build/javafxdoc_debug
	${RM} dist/Debug/javafxdoc.exe

# Subprojects
.clean-subprojects:
