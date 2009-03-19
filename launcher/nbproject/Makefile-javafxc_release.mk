#
# Generated Makefile - do not edit!
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
CC=gcc.exe.lnk
CCC=g++.exe.lnk
CXX=g++.exe.lnk
FC=

# Macros
PLATFORM=Cygwin-Windows

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/javafxc_release/${PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/src/configuration.o \
	${OBJECTDIR}/src/javafxc.o \
	${OBJECTDIR}/src/util.o \
	${OBJECTDIR}/src/javafxw.o \
	${OBJECTDIR}/src/javafxdoc.o \
	${OBJECTDIR}/src/javafx.o

# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=-mno-cygwin -s
CXXFLAGS=-mno-cygwin -s

# Fortran Compiler Flags
FFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	${MAKE}  -f nbproject/Makefile-javafxc_release.mk dist/Release/javafxc.exe

dist/Release/javafxc.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Release
	${LINK.cc} -o dist/Release/javafxc ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/src/configuration.o: src/configuration.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -O2 -DPROJECT_JAVAFXC -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/configuration.o src/configuration.cpp

${OBJECTDIR}/src/javafxc.o: src/javafxc.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -O2 -DPROJECT_JAVAFXC -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/javafxc.o src/javafxc.cpp

${OBJECTDIR}/src/util.o: src/util.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -O2 -DPROJECT_JAVAFXC -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/util.o src/util.cpp

${OBJECTDIR}/src/javafxw.o: src/javafxw.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -O2 -DPROJECT_JAVAFXC -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/javafxw.o src/javafxw.cpp

${OBJECTDIR}/src/javafxdoc.o: src/javafxdoc.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -O2 -DPROJECT_JAVAFXC -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/javafxdoc.o src/javafxdoc.cpp

${OBJECTDIR}/src/javafx.o: src/javafx.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -O2 -DPROJECT_JAVAFXC -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/javafx.o src/javafx.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf:
	${RM} -r build/javafxc_release
	${RM} dist/Release/javafxc.exe

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
