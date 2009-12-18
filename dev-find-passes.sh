#!/bin/sh

# See if any tests in a dev-fail run passed instead of failed.

if [ $# != 2 ] ; then
    echo "--Error:  usage:  dev-find-passes.sh  file1 file2"
    echo "  where file1 contains a list of expected failures"
    echo "  and file2 is the output of an ant dev-fail run"
    exit 1
fi
# this is a file containing a list of expected failures from project.properties
expectedFailsList=$1

# This is the output of an ant dev-fail run
testLog=$2

if [ $testLog = '$log' ] ; then
    echo "--Error: usage:  ant dev-find-passes -Dlog=<output file from an ant dev-fail run>"
    exit 1
fi
tmp=./build/test

expected=$tmp/dev-expected
actual=$tmp/dev-actual
diffs=$tmp/dev-diffs
rm -f $expected $actual $diffs

# the input list is all in one line
cat $expectedFailsList | tr " " "\n" | sed -e '/^ *$/d' -e's@.*/\([^/]*\)/@\1/@' -e 's@ .*@@' | sort > $expected

grep 'Testcase:.*FAILED' $testLog | \
   sed -e 's@.*Testcase: @@' -e 's@(.*@@' -e 's@\\@/@g' -e's@.*/\([^/]*\)/@\1/@' | \
   sort \
 > $actual

diff $expected $actual > $diffs
if [ $? != 0 ] ; then
    echo "These tests appear to have passed:"
    fgrep '< ' $diffs
    echo "If you agree, please remove the above tests from project.properties"
fi
