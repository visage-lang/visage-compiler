#!/bin/sh

# See if any tests passed/failed unexpectedly
# - ./build/test/dev-expected-fails contains the list of expected failures
#    created by ant dev-* targets from the lists in project.properties
# - ./build/test/reports/junit-noframes.html  contains the result of the test run

if [ "$1" = -vv ] ; then
    set -x
fi

# dev-expected-fails list in canonical form, eg:
# test/regress/vsgc979.visage
# test/functional/sequences/SeqCompare.visage

##cat build/test/dev-expected-fails | tr " " "\n" | sed -e '/^ *$/d' -e's@.*/\([^/]*\)/@\1/@' -e 's@ .*@@' | sort > build/test/dev-expected-fails1

cat build/test/dev-expected-fails | tr " " "\n" | sed -e '/^ *$/d' -e 's@ .*@@' | sort > build/test/dev-expected-fails1

thisDir=`pwd`
if [ -r c:/ ] ;then
    thisDir=`cygpath -m $thisDir`
fi 

target=`cat build/test/dev-target`
echo "-- target run was: $target"

find test -name \*.visage > ./build/test/allTests

# Note these filenames only have the last of the dirs between the test and test/, and they contain
# / instead of \
#passes=`fgrep '<td>Success' build/test/reports/junit-noframes.html  | sed -e 's@<td>@@' -e 's@<.*@@' | fgrep /`


# This includes successes from junit which have no / in their paths.  These are not filenames, but 
# names of tests inside a file.

passingFiles=`fgrep '<td>Success' build/test/reports/junit-noframes.html  | sed -e 's@<td>@@' -e 's@<.*@@' | fgrep /`
passingTests=`fgrep '<td>Success' build/test/reports/junit-noframes.html  | sed -e 's@<td>@@' -e 's@<.*@@' | fgrep -v /`

# Expand filenames into full test/....xxx.visage form
passes=
if [ ! -z "$passingFiles" ] ; then
    jjxx="$passingFiles"
    for ii in $jjxx ; do
        case $ii in
            # a little opt here so we don't have to search all the regress's
            regress* | should-fail* | currently-failing*)
              passes="$passes test/$ii"
              ;;
            *)
              full=`fgrep $ii ./build/test/allTests`
              if [ $? = 0 ] ; then 
                  # found it
                 passes="$passes $full"
             else
                 echo "--Error: didn't find $ii in build/test/allTests"
                 exit 1
                 #passes="$passes $ii"
             fi
        esac
    done
fi

# all tests run should have passed
# This fail list DOES contain the whole test/... prefix
compilationFails=`grep '^<td>.* errors' build/test/reports/junit-noframes.html | \
  sed -e 's@\\\@/@g' \
    -e 's@.* test/@test/@' \
    -e 's@ in@@' \
    -e 's@<.*@@'`

# another form of compile time fail
compilationFails1=`grep '<td>expected compiler error' build/test/reports/junit-noframes.html | \
  sed -e 's@\\\@/@g' \
    -e 's@</.*@@' \
    -e 's@<td>@test/@'`

compilationFails="$compilationFails $compilationFails1"

# form is ... <td>Output written...  or ...<td>Program output  or ...<td>Expected output
# This fail list DOES contain the whole test/... prefix
runtimeFails=`grep '<td.* output' build/test/reports/junit-noframes.html | \
  sed -e 's@\\\@/@g' \
    -e 's@.* test/@test/@' \
    -e 's@\.visage.*@.visage@'`

# This list only contains dir/testname.visage.  IE, the test/... is missing
runtimeFails1=`grep '<td.*Output written' build/test/reports/junit-noframes.html | \
  sed -e 's@<td>@@' \
    -e 's@\.visage.*@.visage@' \
    -e 's@\\\@/@g'`

# Here is another form of failure.  This one only contains testname.visage.
# <br>junit.framework.AssertionFailedError: expected:<[ 900 ]> but was:<[ 90 ]><br/>	at bindIfSelect$1local_klass$3.doit$$2(bindIfSelect.visage:188)<br/>	at bindIfSelect.testBoundSelectInverse(bindIfSelect.visage:189)<br/>	at framework.VisageUnitTestWrapper.runTest(VisageUnitTestWrapper.java:86)<br/></code></td><td>0.000</td>

runtimeFails2=`fgrep 'junit.framework.AssertionFailedError: expected' build/test/reports/junit-noframes.html | \
    sed -e 's@\.visage.*@.visage@' -e 's@.*<br/>@@' -e 's@.*(@@'`

runtimeFails1="$runtimeFails1 $runtimeFails2"

# yet another form of failure:
# <br>junit.framework.ComparisonFailure: null expected:<A[E]C> but was:<A[]C><br/>	at MxOnSeq01.testA02(MxOnSeq01.visage:70)<br/>	at framework.VisageUnitTestWrapper.runTest(VisageUnitTestWrapper.java:86)<br/></code></td><td>0.000</td>

runtimeFails2=`fgrep 'junit.framework.ComparisonFailure' build/test/reports/junit-noframes.html | \
    sed -e 's@\.visage.*@.visage@' -e 's@.*<br/>@@' -e 's@.*(@@'`

runtimeFails1="$runtimeFails1 $runtimeFails2"

# put full paths into runtimeFails1   
if [ ! -z "$runtimeFails1" ] ; then
    jjxx="$runtimeFails1"
    runtimeFails1=
    for ii in $jjxx ; do
        full=`fgrep $ii ./build/test/allTests`
        if [ $? = 0 ] ; then
            # found it
            runtimeFails1="$runtimeFails1 $full"
        else
            runtimeFails1="$runtimeFails1 $ii"
        fi
    done
fi

      echo "$compilationFails" | sed -e 's@^ @@' -e 's@ $@@' | sed -e 's@ @\
@g' | sort | uniq > ./build/test/dev-compilation-fails

      echo "$runtimeFails" | sed -e 's@^ @@' -e 's@ $@@' | sed -e 's@ @\
@g' | sort | uniq > ./build/test/dev-runtime-fails

      echo "$runtimeFails1" | sed -e 's@^ @@' -e 's@ $@@' | sed -e 's@ @\
@g' | sort | uniq > ./build/test/dev-runtime-fails1

      # count failures
      echo "$compilationFails $runtimeFails $runtimeFails1" | sed -e 's@^ *@@' -e 's@ *$@@' -e '/^$/d' -e 's@ @\
@g' | sort | uniq > ./build/test/dev-actual-fails
     nFails=`cat ./build/test/dev-actual-fails | wc -l`

     # count passes
     if [ ! -z "$passes" -o ! -z "$passingTests" ] ; then
         echo $passes $passingTests  | sed -e 's@ @\
@g' | sort | uniq > ./build/test/dev-passes
         nPasses=`cat ./build/test/dev-passes | wc -l`
     else
         nPasses=0
     fi
     echo
     echo "--" `expr $nPasses + $nFails` " tests run"


     diff ./build/test/dev-expected-fails1 ./build/test/dev-actual-fails | fgrep '> test' | sed -e 's@>@@' > ./build/test/dev-unexpected-fails
     if [ "$target" != dev-fail ] ; then
         # display failures
         nUnexpectedFails=`cat ./build/test/dev-unexpected-fails | wc -l`
         echo "-- $nFails total failures, $nUnexpectedFails unexpected failures:"
         cat ./build/test/dev-unexpected-fails
         echo
     fi
     if [ "$target" != dev-pass ] ; then 
         # display passes
         # Note that the tests in $passes don't include the full pathnames
         # An unexpected pass is on the pass list and on the actual fail list
         rm -f ./build/test/dev-unexpected-passes
         touch ./build/test/dev-unexpected-passes

         for ii in `cat ./build/test/dev-expected-fails1` ; do
             echo "$passes" | fgrep " $ii" > /dev/null 2>&1
             if [ $? = 0 ] ; then
                 echo $ii >> ./build/test/dev-unexpected-passes
             fi
         done
         nUnexpectedPasses=`cat ./build/test/dev-unexpected-passes | wc -l`
         if [ "$target" == dev-fail ] ; then
             # We needed to count all the junit testlets that are inside a single .visage file
             # to get a count that matches hudson.  For dev-fail, there can be some of these,
             # eg, one testlet fails so the .visage file is on the fail list.  But other testlets
             # pass.   We don't want to say there were some total passes and 0 unexpected passes.
             echo "-- $nUnexpectedPasses unexpected passes:"
         else
             echo "-- $nPasses total passes, $nUnexpectedPasses unexpected passes:"
         fi
         cat ./build/test/dev-unexpected-passes | sed -e 's@^@ @'
     fi

     if [ "$target" != dev-pass -a "$target" != dev-xxx ] ; then
         didit=
         for ii in `cat ./build/test/dev-expected-fails` ; do
             fgrep $ii ./build/test/dev-actual-fails ./build/test/dev-passes > /dev/null 2>&1
             if [ $? != 0 ] ; then
                 if [ -z "$didit" ] ; then
                     echo
                     echo "--Expected failures for which pass/fail status cannot be determined"
                     didit=1
                 fi
                 echo "   $ii"
             fi
         done
     fi

###  This shows a number of non VisageCompilerTest tests run; don't know why 
###     echo "-- Result URL: file:///$thisDir/build/test/reports/junit-noframes.html"

     # This only works if we only ran the fail list tests.
     # diff ./build/test/dev-expected-fails1 ./build/test/dev-actual-fails | fgrep '< test'

if [ $# != 0 ] ; then
    # get all the tests from 3555, 3561, and those from ABORT into allknown, and this will
    # then find tests that fail but are not in any of the above.
    if [ ! -r allknown ] ; then
        echo "  Get all the tests from 3555, 3561, and those with ABORT into ./allknown"
        echo "  and this will show failures that are in none of them"
        exit 1
    fi
    for ii in `cat ./build/test/dev-actual-fails` ; do
        fgrep $ii allknown > /dev/null 2>&1
        if [ $? != 0 ] ; then
           echo "Not found: " $ii
       fi
    done
fi
exit
