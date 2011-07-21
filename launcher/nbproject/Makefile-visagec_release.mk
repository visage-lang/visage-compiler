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
CC=
CCC=g++.exe
CXX=g++.exe
FC=

# Macros
PLATFORM=Cygwin-Windows

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/visagec_release/${PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/src/configuration.o \
	${OBJECTDIR}/src/visagec.o \
	${OBJECTDIR}/src/util.o \
	${OBJECTDIR}/src/visagew.o \
	${OBJECTDIR}/src/visagedoc.o \
	${OBJECTDIR}/src/visage.o

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
	${MAKE}  -f nbproject/Makefile-visagec_release.mk dist/Release/visagec.exe

dist/Release/visagec.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Release
	${LINK.cc} -o dist/Release/visagec ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/src/configuration.o: src/configuration.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -O2 -DPROJECT_JAVAFXC -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/configuration.o src/configuration.cpp

${OBJECTDIR}/src/visagec.o: src/visagec.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -O2 -DPROJECT_JAVAFXC -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/visagec.o src/visagec.cpp

${OBJECTDIR}/src/util.o: src/util.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -O2 -DPROJECT_JAVAFXC -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/util.o src/util.cpp

${OBJECTDIR}/src/visagew.o: src/visagew.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -O2 -DPROJECT_JAVAFXC -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/visagew.o src/visagew.cpp

${OBJECTDIR}/src/visagedoc.o: src/visagedoc.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -O2 -DPROJECT_JAVAFXC -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/visagedoc.o src/visagedoc.cpp

${OBJECTDIR}/src/visage.o: src/visage.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -O2 -DPROJECT_JAVAFXC -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/visage.o src/visage.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf:
	${RM} -r build/visagec_release
	${RM} dist/Release/visagec.exe

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
