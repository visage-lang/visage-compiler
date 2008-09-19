/*
 * Copyright 1999-2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
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

package com.sun.tools.javafx.code;
import static com.sun.tools.javac.code.Flags.*;

/**
 * Some Javafx-specific flags for {@code Symbols}'s {@code flags_field}.
 *
 * @author Robert Field
 * @author Per Bothner
 * @author Lubo Litchev
 */
public class JavafxFlags {
    private JavafxFlags() {}

    private static final long LAST_JAVA_FLAG = PROPRIETARY;

    // Explicit (parser set) flags
    private static final long FIRST_FX_MOD_FLAG        = LAST_JAVA_FLAG << 1;
    public static final long BOUND                     = FIRST_FX_MOD_FLAG << 0;  // bound function
    public static final long OVERRIDE                  = FIRST_FX_MOD_FLAG << 1;  // overridden function
    public static final long IS_DEF                    = FIRST_FX_MOD_FLAG << 2;  // 'def' variable
    public static final long PUBLIC_READ               = FIRST_FX_MOD_FLAG << 3;  // public-read var
    public static final long PUBLIC_INIT               = FIRST_FX_MOD_FLAG << 4;  // public-init var
    public static final long PACKAGE_ACCESS            = FIRST_FX_MOD_FLAG << 5;  // explicit 'package' access
    private static final long LAST_FX_MOD_FLAG         = PACKAGE_ACCESS;

    // misc FX flags
    private static final long FIRST_FX_MISC_FLAG       = LAST_FX_MOD_FLAG << 1;
    public static final long SCRIPT_PRIVATE            = FIRST_FX_MISC_FLAG << 0;  // implicily set flag if public/protected/package are not
    public static final long SCRIPT_LEVEL_SYNTH_STATIC = FIRST_FX_MISC_FLAG << 1;  // STATIC bit has been set implicitly
    public static final long IN_INITIALIZER            = FIRST_FX_MISC_FLAG << 2;  // temporary flag set while in var init expression
    private static final long LAST_FX_MISC_FLAG        = IN_INITIALIZER;

    // Var/def usage info -- all usage info is within the script only
    private static final long FIRST_VARUSE_FLAG      = LAST_FX_MISC_FLAG << 1;
    public static final long VARUSE_BOUND_INIT       = FIRST_VARUSE_FLAG << 0;  // defined as bound, initially, in obj lit, or override
    public static final long VARUSE_HAS_ON_REPLACE   = FIRST_VARUSE_FLAG << 1;  // has 'on replace' either in definition or override
    public static final long VARUSE_USED_IN_BIND     = FIRST_VARUSE_FLAG << 2;  // used in a bound expression
    public static final long VARUSE_ASSIGNED_TO      = FIRST_VARUSE_FLAG << 3;  // assigned ("=") to outside of an init
    public static final long VARUSE_INIT_ASSIGNED_TO = FIRST_VARUSE_FLAG << 4;  // assigned to inside of an init
    public static final long VARUSE_OBJ_LIT_INIT     = FIRST_VARUSE_FLAG << 5;  // initialized in an obj lit, bound or not
    public static final long VARUSE_OVERRIDDEN       = FIRST_VARUSE_FLAG << 6;  // var overridden in a subclass
    public static final long VARUSE_INNER_ACCESS     = FIRST_VARUSE_FLAG << 6;  // var accessed within an inner class
    public static final long VARUSE_NEED_LOCATION    = FIRST_VARUSE_FLAG << 7;  // var should be represented by a Location
    public static final long VARUSE_NEED_LOCATION_DETERMINED
                                                     = FIRST_VARUSE_FLAG << 8;  // NEED_LOCATION has been computed and set

    // Class flags -- reuse same bits as VARUSE* flags
    private static final long FIRST_FX_CLASS_FLAG    = LAST_FX_MISC_FLAG << 1;
    public static final long COMPOUND_CLASS          = FIRST_FX_CLASS_FLAG << 0;  // class that gets translated to a class and an inteface (MI)
    public static final long FX_CLASS                = FIRST_FX_CLASS_FLAG << 1;  // has 'on replace' either in definition or override

    public static final long JavafxAccessFlags = PUBLIC | PROTECTED | PRIVATE | SCRIPT_PRIVATE;
    public static final long JavafxExplicitAccessFlags = PUBLIC | PROTECTED | PRIVATE | PACKAGE_ACCESS;
    
    public static final long JavafxUserFlags     = JavafxExplicitAccessFlags | STATIC | ABSTRACT | BOUND | OVERRIDE | IS_DEF | PUBLIC_READ | PUBLIC_INIT;
    public static final long JavafxLocalVarFlags = PARAMETER | IS_DEF;
    public static final long JavafxClassVarFlags = JavafxExplicitAccessFlags | STATIC | PUBLIC_READ | PUBLIC_INIT;
    public static final long JavafxClassDefFlags = JavafxExplicitAccessFlags | STATIC | IS_DEF | PUBLIC_READ;
    public static final long JavafxFunctionFlags = JavafxExplicitAccessFlags | STATIC | ABSTRACT | BOUND | OVERRIDE;
    public static final long JavafxClassFlags    = JavafxExplicitAccessFlags | STATIC | ABSTRACT;
}
