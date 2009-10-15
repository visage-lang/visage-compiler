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
            public void update$(FXObject srcObj, int varNum, int phase) {
                numTimesDep1Updated[0]++;
                assertSame(src, srcObj);
                assertEquals(0, varNum);
            }
        };
        src.addDependent$(0, dep1);
        final int[] numTimesDep2Updated = new int[1];
        final FXBase dep2 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum, int phase) {
                numTimesDep2Updated[0]++;
                assertSame(src, srcObj);
                assertEquals(1, varNum);
            }
        };
        src.addDependent$(1, dep2);

        // update zeroth var
        src.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        // dep1's update$ should have been called
        // dep2's update$ should not have been called
        assertEquals(1, numTimesDep1Updated[0]);
        assertEquals(0, numTimesDep2Updated[0]);

        // update first var
        src.notifyDependents$(1, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
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
            public void update$(FXObject srcObj, int varNum, int phase) {
                numTimesDep1Updated[0]++;
                assertSame(src, srcObj);
                assertEquals(0, varNum);
            }
        };
        final int[] numTimesDep2Updated = new int[1];
        final FXBase dep2 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum, int phase) {
                srcObj.addDependent$(0, dep1);
                numTimesDep2Updated[0]++;
                assertSame(src, srcObj);
                assertEquals(1, varNum);
            }
        };

        src.addDependent$(1, dep2);
        src.notifyDependents$(1, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        assertEquals(0, numTimesDep1Updated[0]);
        assertEquals(1, numTimesDep2Updated[0]);

        // dep2's update adds dep1 as dependent
        src.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
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
            public void update$(FXObject srcObj, int varNum, int phase) {
                numTimesDep1Updated[0]++;
                assertSame(src, srcObj);
                assertEquals(0, varNum);
            }
        };
        final int[] numTimesDep2Updated = new int[1];
        final FXBase dep2 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum, int phase) {
                srcObj.removeDependent$(0, dep1);
                numTimesDep2Updated[0]++;
                assertSame(src, srcObj);
                assertEquals(1, varNum);
            }
        };

        src.addDependent$(0, dep1);
        src.addDependent$(1, dep2);

        src.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        src.notifyDependents$(1, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        assertEquals(1, numTimesDep1Updated[0]);
        assertEquals(1, numTimesDep2Updated[0]);

        // dep2's update removed dep1 as dependent
        // so, we should not get dep1.update$ call
        src.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        src.notifyDependents$(1, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
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
            public void update$(FXObject srcObj, int varNum, int phase) {
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
            public void update$(FXObject srcObj, int varNum, int phase) {
                numTimesDep2Updated[0]++;
                assertSame(src, srcObj);
            }
        };

        src.addDependent$(0, dep2);
        src.addDependent$(0, dep1);
        src.addDependent$(1, dep2);
        assertEquals(3, src.getListenerCount$());
        src.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        assertEquals(1, numTimesDep1Updated[0]);
        assertEquals(1, numTimesDep2Updated[0]);
        // one dependent removed from notification loop
        assertEquals(2, src.getListenerCount$());
    }

    public void testRemoveAllDuringNotification() {
        // create an object with one variable
        final FXBase src = new FXBase() {
            @Override
            public int count$() { return 1; }
        };

        final int[] deleter = new int[1];
        final FXBase[] dependents = new FXBase[3];
        final FXBase dep0 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum, int phase) {
                if (deleter[0] == 0) {
                   for (FXBase fx : dependents) {
                       srcObj.removeDependent$(0, fx);
                   }
                }
            }
        };
        final FXBase dep1 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum, int phase) {
                if (deleter[0] == 1) {
                   for (FXBase fx : dependents) {
                       srcObj.removeDependent$(0, fx);
                   }
                }
            }
        };
        final FXBase dep2 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum, int phase) {
                if (deleter[0] == 2) {
                   for (FXBase fx : dependents) {
                       srcObj.removeDependent$(0, fx);
                   }
                }
            }
        };
        dependents[0] = dep0;
        dependents[1] = dep1;
        dependents[2] = dep2;

        src.addDependent$(0, dep0);
        src.addDependent$(0, dep1);
        src.addDependent$(0, dep2);
        assertEquals(3, src.getListenerCount$());
        // remove all from the first inserted dependent
        deleter[0] = 0;
        src.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        assertEquals(0, src.getListenerCount$());

        src.addDependent$(0, dep0);
        src.addDependent$(0, dep1);
        src.addDependent$(0, dep2);
        assertEquals(3, src.getListenerCount$());
        // remove all from the second (middle) inserted dependent
        deleter[0] = 1;
        src.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        assertEquals(0, src.getListenerCount$());

        src.addDependent$(0, dep0);
        src.addDependent$(0, dep1);
        src.addDependent$(0, dep2);
        assertEquals(3, src.getListenerCount$());
        // removal all from the last inserted dependent
        deleter[0] = 2;
        src.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        assertEquals(0, src.getListenerCount$());
    }

    public void testSwitchDependence() {
        // create an object with one variable
        final FXBase src1 = new FXBase() {
            @Override
            public int count$() { return 1; }
        };

        // create an object with one variable
        final FXBase src2 = new FXBase() {
            @Override
            public int count$() { return 1; }
        };

        // check that update$ method is called as expected
        final int[] numTimesDepUpdated = new int[1];
        final FXBase dep = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum, int phase) {
                numTimesDepUpdated[0]++;
                assertEquals(0, varNum);
            }
        };

        // no listeners for both sources
        assertEquals(0, src1.getListenerCount$());
        assertEquals(0, src2.getListenerCount$());

        // add one listener for "src1"
        src1.addDependent$(0, dep);
        assertEquals(1, src1.getListenerCount$());
        src1.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        assertEquals(1, numTimesDepUpdated[0]);

        // switch the dependence of "dep" from "src1" to "src2"
        dep.switchDependence$(src1, 0, src2, 0);
        assertEquals(0, src1.getListenerCount$());
        assertEquals(1, src2.getListenerCount$());

        src2.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        assertEquals(2, numTimesDepUpdated[0]);
    }

    public void testSwitchCurrentDuringNotification() {
        final FXBase src1 = new FXBase() {
            @Override
            public int count$() { return 1; }
        };

        final FXBase src2 = new FXBase() {
            @Override
            public int count$() { return 1; }
        };

        final FXBase dep = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum, int phase) {
                // switch dependence of current object
                this.switchDependence$(src1, 0, src2, 0);
            }
        };
        src1.addDependent$(0, dep);
        assertEquals(1, src1.getListenerCount$());
        assertEquals(0, src2.getListenerCount$());
        src1.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        assertEquals(0, src1.getListenerCount$());
        assertEquals(1, src2.getListenerCount$());
    }

    public void testSwitchAllDuringNotification() {
        final FXBase src1 = new FXBase() {
            @Override
            public int count$() { return 1; }
        };
        final FXBase src2 = new FXBase() {
            @Override
            public int count$() { return 1; }
        };

        final int[] switcher = new int[1];
        final FXBase[] dependents = new FXBase[3];
        final FXBase dep0 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum, int phase) {
                if (switcher[0] == 0) {
                   for (FXBase fx : dependents) {
                       fx.switchDependence$(src1, 0, src2, 0);
                   }
                }
            }
        };
        final FXBase dep1 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum, int phase) {
                if (switcher[0] == 1) {
                   for (FXBase fx : dependents) {
                       fx.switchDependence$(src1, 0, src2, 0);
                   }
                }
            }
        };
        final FXBase dep2 = new FXBase() {
            @Override
            public void update$(FXObject srcObj, int varNum, int phase) {
                if (switcher[0] == 2) {
                   for (FXBase fx : dependents) {
                       fx.switchDependence$(src1, 0, src2, 0);
                   }
                }
            }
        };

        assertEquals(0, src1.getListenerCount$());
        assertEquals(0, src2.getListenerCount$());

        dependents[0] = dep0;
        dependents[1] = dep1;
        dependents[2] = dep2;
        src1.addDependent$(0, dep0);
        src1.addDependent$(0, dep1);
        src1.addDependent$(0, dep2);
        assertEquals(3, src1.getListenerCount$());
        // switch all from the first inserted dependent
        switcher[0] = 0;
        src1.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        assertEquals(0, src1.getListenerCount$());
        assertEquals(3, src2.getListenerCount$());
        for (FXObject d : dependents) {
            src2.removeDependent$(0, d);
        }

        src1.addDependent$(0, dep0);
        src1.addDependent$(0, dep1);
        src1.addDependent$(0, dep2);
        assertEquals(3, src1.getListenerCount$());
        assertEquals(0, src2.getListenerCount$());
        // switch all from the second (middle) inserted dependent
        switcher[0] = 1;
        src1.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        assertEquals(0, src1.getListenerCount$());
        assertEquals(3, src2.getListenerCount$());
        for (FXObject d : dependents) {
            src2.removeDependent$(0, d);
        }

        src1.addDependent$(0, dep0);
        src1.addDependent$(0, dep1);
        src1.addDependent$(0, dep2);
        assertEquals(3, src1.getListenerCount$());
        // switch all from the last inserted dependent
        switcher[0] = 2;
        src1.notifyDependents$(0, FXObject.VFLGS$IS_VALID_INVAL_PHASE);
        assertEquals(0, src1.getListenerCount$());
        assertEquals(3, src2.getListenerCount$());
    }
}

