#!/bin/sh

# See if any tests passed/failed unexpectedly
# - ./build/test/dev-expected-fails contains the list of expected failures
#    created by ant dev-* targets from the lists in project.properties
# - ./build/test/reports/junit-noframes.html  contains the result of the test run

if [ "$1" = -vv ] ; then
    set -x
fi

# dev-expected-fails list in canonical form, eg:
# test/regress/jfxc979.fx
# test/functional/sequences/SeqCompare.fx

##cat build/test/dev-expected-fails | tr " " "\n" | sed -e '/^ *$/d' -e's@.*/\([^/]*\)/@\1/@' -e 's@ .*@@' | sort > build/test/dev-expected-fails1

cat build/test/dev-expected-fails | tr " " "\n" | sed -e '/^ *$/d' -e 's@ .*@@' | sort > build/test/dev-expected-fails1

thisDir=`pwd`
if [ -r c:/ ] ;then
    thisDir=`cygpath -m $thisDir`
fi 

target=`cat build/test/dev-target`
echo "-- target run was: $target"

find test -name \*.fx > ./build/test/allTests

# Note these filenames only have the last of the dirs between the test and test/, and they contain
# / instead of \
#passes=`fgrep '<td>Success' build/test/reports/junit-noframes.html  | sed -e 's@<td>@@' -e 's@<.*@@' | fgrep /`


# This includes successes from junit which have no / in their paths.  These are not filenames, but 
# names of tests inside a file.

passingFiles=`fgrep '<td>Success' build/test/reports/junit-noframes.html  | sed -e 's@<td>@@' -e 's@<.*@@' | fgrep /`
passingTests=`fgrep '<td>Success' build/test/reports/junit-noframes.html  | sed -e 's@<td>@@' -e 's@<.*@@' | fgrep -v /`

# Expand filenames into full test/....xxx.fx form
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

# form is ... <td>Output written...  or ...<td>Program output  or ...<td>Expected output
# This fail list DOES contain the whole test/... prefix
runtimeFails=`grep '<td.* output' build/test/reports/junit-noframes.html | \
  sed -e 's@\\\@/@g' \
    -e 's@.* test/@test/@' \
    -e 's@\.fx.*@.fx@'`

# This list only contains dir/testname.fx.  IE, the test/... is missing
runtimeFails1=`grep '<td.*Output written' build/test/reports/junit-noframes.html | \
  sed -e 's@<td>@@' \
    -e 's@\.fx.*@.fx@' \
    -e 's@\\\@/@g'`

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

# Here is another form of failure -  we don't handle this one:
#<tr valign="top" class="Error">
#<td>testMethodOverloadInstance</td><td>Failure</td><td>null expected:&lt;[jj]&gt; but was:&lt;[kk]&gt;<code>
#<br>
#<br>junit.framework.ComparisonFailure: null expected:<[jj]> but was:<[kk]><br/>	at MethodOverload$TesterInstance.testInstanceRetInt(MethodOverload.fx:175)<br/>	at MethodOverload.testMethodOverloadInstance(MethodOverload.fx:336)<br/>	at framework.FXUnitTestWrapper.runTest(FXUnitTestWrapper.java:86)<br/></code></td><td>0.000</td>
#</tr>
# The test is MethodOverload.fx but it contains several subtests.  One of them can fail, but that doesn't cause MethodOverload itself to fail.

      echo "$compilationFails" | sed -e 's@^ @@' -e 's@ $@@' | sed -e 's@ @\
@g' | sort > ./build/test/dev-compilation-fails

      echo "$runtimeFails" | sed -e 's@^ @@' -e 's@ $@@' | sed -e 's@ @\
@g' | sort > ./build/test/dev-runtime-fails

      echo "$runtimeFails1" | sed -e 's@^ @@' -e 's@ $@@' | sed -e 's@ @\
@g' | sort > ./build/test/dev-runtime-fails1

      # count failures
      echo "$compilationFails $runtimeFails $runtimeFails1" | sed -e 's@^ *@@' -e 's@ *$@@' -e '/^$/d' -e 's@ @\
@g' | sort > ./build/test/dev-actual-fails
     nFails=`cat ./build/test/dev-actual-fails | wc -l`

     # count passes
     if [ ! -z "$passes" -o ! -z "$passingTests" ] ; then
         echo $passes $passingTests  | sed -e 's@ @\
@g' | sort > ./build/test/dev-passes
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
         echo "-- $nPasses total passes, $nUnexpectedPasses unexpected passes:"
         cat ./build/test/dev-unexpected-passes | sed -e 's@^@ @'
     fi

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

###  This shows a number of non FXCompilerTest tests run; don't know why 
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
