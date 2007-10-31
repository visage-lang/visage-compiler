import java.lang.System;

/*
 * @test
 * @run
 */

class X {
    attribute nums: Number[];
    attribute xs: Number[] = bind nums
        on replace[idx](value) {
            System.out.println("replace {idx} {value}");
        }
        on delete[idx](value) {
            System.out.println("delete {idx} {value}");
        }
        on insert[idx](value) {
            System.out.println("insert {idx} {value}");
        }
}

var x = X {
    nums: [1.0, 2.0, 3.0]
};

delete x.nums[0];
x.nums[0] = 20.0;
insert 99.0 into x.nums;
