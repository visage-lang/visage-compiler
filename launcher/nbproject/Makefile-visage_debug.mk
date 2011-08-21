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
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc
CCC=g++-3
CXX=g++-3
FC=gfortran
AS=as

# Macros
CND_PLATFORM=Cygwin_4.x-Windows
CND_CONF=visage_debug
CND_DISTDIR=dist
CND_BUILDDIR=build

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=${CND_BUILDDIR}/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/src/visagedoc.o \
	${OBJECTDIR}/src/visage.o \
	${OBJECTDIR}/src/visagec.o \
	${OBJECTDIR}/src/visagew.o \
	${OBJECTDIR}/src/util.o \
	${OBJECTDIR}/src/configuration.o


# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=-mno-cygwin
CXXFLAGS=-mno-cygwin

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk dist/Debug/visage.exe

dist/Debug/visage.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Debug
	${LINK.cc} -o dist/Debug/visage ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/src/visagedoc.o: src/visagedoc.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFX -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/visagedoc.o src/visagedoc.cpp

${OBJECTDIR}/src/visage.o: src/visage.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFX -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/visage.o src/visage.cpp

${OBJECTDIR}/src/visagec.o: src/visagec.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFX -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/visagec.o src/visagec.cpp

${OBJECTDIR}/src/visagew.o: src/visagew.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFX -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/visagew.o src/visagew.cpp

${OBJECTDIR}/src/util.o: src/util.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFX -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/util.o src/util.cpp

${OBJECTDIR}/src/configuration.o: src/configuration.cpp 
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} $@.d
	$(COMPILE.cc) -g -Wall -DPROJECT_JAVAFX -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/src/configuration.o src/configuration.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r ${CND_BUILDDIR}/${CND_CONF}
	${RM} dist/Debug/visage.exe

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
