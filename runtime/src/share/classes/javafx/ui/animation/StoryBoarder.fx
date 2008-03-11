/*
 * Copyright 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */ 

package javafx.ui.animation;

import java.lang.Object;
import com.sun.tools.javafx.ui.animation.Animation;
import com.sun.tools.javafx.ui.animation.Animation.Animator;
import com.sun.tools.javafx.ui.animation.Animation.Getter;
import com.sun.tools.javafx.ui.animation.Animation.Evaluator;
import com.sun.tools.javafx.ui.animation.Animation.ActionListener;
import com.sun.javafx.runtime.Pointer;
import java.util.*;

// hack to work around compiler bugs
class StoryBoarder {
    attribute sbList: List;
    attribute animator: Animator;
    attribute s: Timeline;
    attribute storyStart:Number;
    function doStoryboard(): KeyFrame {
        var sb = animator.createStoryBoard();
        sb.setRepeatCount(s.repeatCount);
        sb.setAutoReverse(s.autoReverse);
	var klist = new ArrayList();
	var lastKeyFrame:KeyFrame = null;
        var startTime:Number = 0;
        for (i in s.keyFrames) {
            var list = new LinkedList();
            for (j in i.keyValues) {
                var k = animator.createKeyValue(j.target,
                                                j.getValue(),
                                                Evaluator {
                                                    public function evaluate(prop:Object, oldValue:Object, newValue:Object, t:Number):Object {
                                                        Timeline.setTarget(j.target,
                                                                           j.getInterpolatedValue(oldValue, t));;
                                                        
                                                        return newValue;
                                                    }
                                                });
                list.add(k);
            }
            var k1 = animator.createKeyFrame();
            k1.setKeyValues(list);
            klist.add(k1);
            k1.setActionListener(ActionListener {
                public function doAction():Void {
                    if (i.action <> null) {
                        i.action();
                    }
                }
            });
            if (i.relative) {
                var off = if (lastKeyFrame == null) 0 else startTime + i.keyTime.toMillis();
                startTime = off;
            } else {
                startTime = i.keyTime.toMillis();
            }
            k1.setKeyTime((storyStart + startTime).longValue());
            var kf = i;
            var kf1: KeyFrame = kf;
            for (j in i.timelines) {
                var storyboarder = StoryBoarder {
                    sbList: sbList;
                    animator: animator;
                    s: j
                    storyStart: startTime + j.startTime.toMillis()
                };
                kf1 = storyboarder.doStoryboard();
            }
            lastKeyFrame = kf1;
        }
        sb.setKeyFrames(klist);
        sbList.add(sb);
        return lastKeyFrame;
    }
}
