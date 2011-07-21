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
OBJECTDIR=build/visagew_debug/${PLATFORM}

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
CCFLAGS=-mno-cygwin
CXXFLAGS=-mno-cygwin

# Fortran Compiler Flags
FFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	${MAKE}  -f nbproject/Makefile-visagew_debug.mk dist/Debug/visagew.exe

dist/Debug/visagew.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Debug
	${LINK.cc} -o dist/Debug/visagew ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/src/configuration.o: src/configuration.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFXW -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/configuration.o src/configuration.cpp

${OBJECTDIR}/src/visagec.o: src/visagec.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFXW -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/visagec.o src/visagec.cpp

${OBJECTDIR}/src/util.o: src/util.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFXW -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/util.o src/util.cpp

${OBJECTDIR}/src/visagew.o: src/visagew.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFXW -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/visagew.o src/visagew.cpp

${OBJECTDIR}/src/visagedoc.o: src/visagedoc.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFXW -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/visagedoc.o src/visagedoc.cpp

${OBJECTDIR}/src/visage.o: src/visage.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFXW -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/visage.o src/visage.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf:
	${RM} -r build/visagew_debug
	${RM} dist/Debug/visagew.exe

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
