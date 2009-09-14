package com.sun.javafx.runtime;

/**
 * This class tests dependents maintainence for FXBase instances.
 */
public class DependentsTest extends JavaFXTestCase {
    public void testAddRemove() {
        // create an FXBase with 2 fields.
        FXBase src = new FXBase() {
            @Override
            public int count$() { return 2; }
        };
        // fresh object, no dependents yet
        assertEquals(0, src.getListenerCount$());

        // create two dependent objects
        FXBase dep1 = new FXBase();
        FXBase dep2 = new FXBase();

        // check dependent addition
        src.addDependent$(0, dep1);
        assertEquals(1, src.getListenerCount$());
        src.addDependent$(1, dep1);
        assertEquals(2, src.getListenerCount$());
        src.addDependent$(0, dep2);
        assertEquals(3, src.getListenerCount$());
        src.addDependent$(1, dep2);
        assertEquals(4, src.getListenerCount$());

        // check removals
        src.removeDependent$(0, dep1);
        assertEquals(3, src.getListenerCount$());
        src.removeDependent$(1, dep1);
        assertEquals(2, src.getListenerCount$());
        src.removeDependent$(1, dep2);
        assertEquals(1, src.getListenerCount$());
        src.removeDependent$(0, dep2);
        assertEquals(0, src.getListenerCount$());
    }

    public void testUpdate() {
        // create an object with two variables
        final FXBase src = new FXBase() {
            @Override
            public int count$() { return 2; }
        };

        // check that update$ method is called as expected
        final int[] numTimesDep1Updated = new int[1];
        // create two dependents and register for different 'varNum's.
        final FXBase dep1 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum) {
                numTimesDep1Updated[0]++;
                assertSame(src, srcObj);
                assertEquals(0, varNum);
            }
        };
        src.addDependent$(0, dep1);
        final int[] numTimesDep2Updated = new int[1];
        final FXBase dep2 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum) {
                numTimesDep2Updated[0]++;
                assertSame(src, srcObj);
                assertEquals(1, varNum);
            }
        };
        src.addDependent$(1, dep2);

        // update zeroth var
        src.notifyDependents$(0);
        // dep1's update$ should have been called
        // dep2's update$ should not have been called
        assertEquals(1, numTimesDep1Updated[0]);
        assertEquals(0, numTimesDep2Updated[0]);

        // update first var
        src.notifyDependents$(1);
        // dep1's update$ should not have been called
        // dep2's update$ should have been called
        assertEquals(1, numTimesDep1Updated[0]);
        assertEquals(1, numTimesDep2Updated[0]);
    }

    public void testAddDuringNotification() {
        // create an object with two variables
        final FXBase src = new FXBase() {
            @Override
            public int count$() { return 2; }
        };

        // check that update$ method is called as expected
        final int[] numTimesDep1Updated = new int[1];
        // create two dependents and register for different 'varNum's.
        final FXBase dep1 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum) {
                numTimesDep1Updated[0]++;
                assertSame(src, srcObj);
                assertEquals(0, varNum);
            }
        };
        final int[] numTimesDep2Updated = new int[1];
        final FXBase dep2 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum) {
                srcObj.addDependent$(0, dep1);
                numTimesDep2Updated[0]++;
                assertSame(src, srcObj);
                assertEquals(1, varNum);
            }
        };

        src.addDependent$(1, dep2);
        src.notifyDependents$(1);
        assertEquals(0, numTimesDep1Updated[0]);
        assertEquals(1, numTimesDep2Updated[0]);

        // dep2's update adds dep1 as dependent
        src.notifyDependents$(0);
        assertEquals(1, numTimesDep1Updated[0]);
        assertEquals(1, numTimesDep2Updated[0]);
    }

    public void testRemoveDuringNotification() {
        // create an object with two variables
        final FXBase src = new FXBase() {
            @Override
            public int count$() { return 2; }
        };

        // check that update$ method is called as expected
        final int[] numTimesDep1Updated = new int[1];
        // create two dependents and register for different 'varNum's.
        final FXBase dep1 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum) {
                numTimesDep1Updated[0]++;
                assertSame(src, srcObj);
                assertEquals(0, varNum);
            }
        };
        final int[] numTimesDep2Updated = new int[1];
        final FXBase dep2 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum) {
                srcObj.removeDependent$(0, dep1);
                numTimesDep2Updated[0]++;
                assertSame(src, srcObj);
                assertEquals(1, varNum);
            }
        };

        src.addDependent$(0, dep1);
        src.addDependent$(1, dep2);

        src.notifyDependents$(0);
        src.notifyDependents$(1);
        assertEquals(1, numTimesDep1Updated[0]);
        assertEquals(1, numTimesDep2Updated[0]);

        // dep2's update removed dep1 as dependent
        // so, we should not get dep1.update$ call
        src.notifyDependents$(0);
        src.notifyDependents$(1);
        assertEquals(1, numTimesDep1Updated[0]);
        assertEquals(2, numTimesDep2Updated[0]);
    }

    public void testRemoveCurrentDuringNotification() {
        // create an object with two variables
        final FXBase src = new FXBase() {
            @Override
            public int count$() { return 2; }
        };

        final int[] numTimesDep1Updated = new int[1];
        // create two dependents and register for different 'varNum's.
        final FXBase dep1 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum) {
                numTimesDep1Updated[0]++;
                assertSame(src, srcObj);
                assertEquals(0, varNum);
                srcObj.removeDependent$(0, this);
            }
        };

        final int[] numTimesDep2Updated = new int[1];
        // create two dependents and register for different 'varNum's.
        final FXBase dep2 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum) {
                numTimesDep2Updated[0]++;
                assertSame(src, srcObj);
            }
        };

        src.addDependent$(0, dep2);
        src.addDependent$(0, dep1);
        src.addDependent$(1, dep2);
        assertEquals(3, src.getListenerCount$());
        src.notifyDependents$(0);
        assertEquals(1, numTimesDep1Updated[0]);
        assertEquals(1, numTimesDep2Updated[0]);
        // one dependent removed from notification loop
        assertEquals(2, src.getListenerCount$());
    }

    public void testRemoveAllDuringNotification() {
        // create an object with two variables
        final FXBase src = new FXBase() {
            @Override
            public int count$() { return 1; }
        };


        final boolean[] delete = new boolean[4];
        final FXBase[] dependents = new FXBase[4];
        final FXBase dep0 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum) {
                if (delete[0]) {
                   for (FXBase fx : dependents) {
                       srcObj.removeDependent$(0, fx);
                   }
                }
            }
        };
        final FXBase dep1 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum) {
                if (delete[1]) {
                   for (FXBase fx : dependents) {
                       srcObj.removeDependent$(0, fx);
                   }
                }
            }
        };
        final FXBase dep2 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum) {
                if (delete[2]) {
                   for (FXBase fx : dependents) {
                       srcObj.removeDependent$(0, fx);
                   }
                }
            }
        };
        final FXBase dep3 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum) {
                if (delete[3]) {
                   for (FXBase fx : dependents) {
                       srcObj.removeDependent$(0, fx);
                   }
                }
            }
        };
        dependents[0] = dep0;
        dependents[1] = dep1;
        dependents[2] = dep2;
        dependents[3] = dep3;

        src.addDependent$(0, dep0);
        src.addDependent$(0, dep1);
        src.addDependent$(0, dep2);
        src.addDependent$(0, dep3);
        assertEquals(4, src.getListenerCount$());
        // delete all from first inserted dependent
        delete[0] = true;
        delete[1] = false;
        delete[2] = false;
        delete[3] = false;
        src.notifyDependents$(0);
        assertEquals(0, src.getListenerCount$());

        src.addDependent$(0, dep0);
        src.addDependent$(0, dep1);
        src.addDependent$(0, dep2);
        src.addDependent$(0, dep3);
        assertEquals(4, src.getListenerCount$());
        // delete all from middle inserted dependent
        delete[0] = false;
        delete[1] = true;
        delete[2] = false;
        delete[3] = false;
        src.notifyDependents$(0);
        assertEquals(0, src.getListenerCount$());

        src.addDependent$(0, dep0);
        src.addDependent$(0, dep1);
        src.addDependent$(0, dep2);
        src.addDependent$(0, dep3);
        assertEquals(4, src.getListenerCount$());
        // delete all from middle inserted dependent
        delete[0] = false;
        delete[1] = false;
        delete[2] = true;
        delete[3] = false;
        src.notifyDependents$(0);
        assertEquals(0, src.getListenerCount$());

        src.addDependent$(0, dep0);
        src.addDependent$(0, dep1);
        src.addDependent$(0, dep2);
        src.addDependent$(0, dep3);
        assertEquals(4, src.getListenerCount$());
        // delete all from last inserted dependent
        delete[0] = false;
        delete[1] = false;
        delete[2] = false;
        delete[3] = true;
        src.notifyDependents$(0);
        assertEquals(0, src.getListenerCount$());
    }
}
