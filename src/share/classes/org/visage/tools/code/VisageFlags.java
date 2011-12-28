/*
 * Copyright 2008-2009 Sun Microsystems, Inc.  All Rights Reserved.
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

package org.visage.tools.code;
import static com.sun.tools.mjavac.code.Flags.*;

/**
 * Some Visage-specific flags for {@code Symbols}'s {@code flags_field}.
 *
 * @author Robert Field
 * @author Per Bothner
 * @author Lubo Litchev
 */
public class VisageFlags {
    private VisageFlags() {}

    private static final long LAST_JAVA_FLAG = PROPRIETARY;

    // Explicit (parser set) modifier flags
    private static final long FIRST_VISAGE_MOD_FLAG        = LAST_JAVA_FLAG << 1;
    public static final long BOUND                     = FIRST_VISAGE_MOD_FLAG << 0;  // bound function
    public static final long DEFAULT                   = FIRST_VISAGE_MOD_FLAG << 1;  // default var
    public static final long OVERRIDE                  = FIRST_VISAGE_MOD_FLAG << 2;  // overridden function
    public static final long IS_DEF                    = FIRST_VISAGE_MOD_FLAG << 3;  // 'def' variable
    public static final long PUBLIC_READ               = FIRST_VISAGE_MOD_FLAG << 4;  // public-read var
    public static final long PUBLIC_INIT               = FIRST_VISAGE_MOD_FLAG << 5;  // public-init var
    public static final long PACKAGE_ACCESS            = FIRST_VISAGE_MOD_FLAG << 6;  // explicit 'package' access
    public static final long MIXIN                     = FIRST_VISAGE_MOD_FLAG << 7;  // this is a mixin class

    // a couple of synthetic modifier flags
    public static final long SCRIPT_PRIVATE            = FIRST_VISAGE_MOD_FLAG << 8;  // implicily set flag if public/protected/package are not
    public static final long SCRIPT_LEVEL_SYNTH_STATIC = FIRST_VISAGE_MOD_FLAG << 9;  // STATIC bit has been set implicitly
    public static final long OBJ_LIT_INIT              = FIRST_VISAGE_MOD_FLAG << 10;
    private static final long LAST_VISAGE_MOD_FLAG         = OBJ_LIT_INIT;

    // Flags on vars to inform translation
    private static final long FIRST_VARMARK_FLAG       = LAST_VISAGE_MOD_FLAG << 1;
    public static final long VARMARK_BARE_SYNTH        = FIRST_VARMARK_FLAG << 0; // synthetic that should only have field and invalidate generated for it (no accessors)
    private static final long LAST_VARMARK_FLAG        = VARMARK_BARE_SYNTH;

    // Var/def usage info -- all usage info is within the script only 
    private static final long FIRST_VARUSE_FLAG        = LAST_VARMARK_FLAG << 1;
    public static final long VARUSE_HAS_REPLACE_TRIGGER        = FIRST_VARUSE_FLAG << 0;  // has 'on replace' either in definition or override
    public static final long VARUSE_BOUND_INIT         = FIRST_VARUSE_FLAG << 1;  // defined as bound, initially, in obj lit, or override
    public static final long VARUSE_ASSIGNED_TO        = FIRST_VARUSE_FLAG << 2;  // assigned ("=") to
    public static final long VARUSE_OBJ_LIT_INIT       = FIRST_VARUSE_FLAG << 3;  // initialized in an obj lit, bound or not
    public static final long VARUSE_FORWARD_REFERENCE  = FIRST_VARUSE_FLAG << 4;  // used before referenced
    public static final long VARUSE_SELF_REFERENCE     = FIRST_VARUSE_FLAG << 5;  // the initializing expression references the var
    public static final long VARUSE_OPT_TRIGGER        = FIRST_VARUSE_FLAG << 6;  // for newElements in trigger: no usage except indexing or sizeof
    public static final long VARUSE_TMP_IN_INIT_EXPR   = FIRST_VARUSE_FLAG << 7;  // temp flag, set while inside var's initializing expression
    public static final long VARUSE_NEED_ACCESSOR      = FIRST_VARUSE_FLAG << 8;  // create accessor methods for this var
    public static final long VARUSE_NON_LITERAL        = FIRST_VARUSE_FLAG << 9;  // non-accessor still needs getter
    public static final long VARUSE_BIND_ACCESS        = FIRST_VARUSE_FLAG << 10; // Accessed in bind.
    public static final long VARUSE_VARREF             = FIRST_VARUSE_FLAG << 11; // Used in VarRef
    public static final long VARUSE_SPECIAL            = FIRST_VARUSE_FLAG << 12; // Ignore in varuse analysis.
    // WARNING - NO MORE BITS
    // This last shift + the last for modifiers + the last on marks must be <= 25 or we get overflow

    // Function flags -- reuse same bits as VARUSE* flags
    private static final long FIRST_VISAGE_FUNC_FLAG    = LAST_VISAGE_MOD_FLAG << 1;
    public static final long FUNC_IS_BUILTINS_SYNTH    = FIRST_VISAGE_FUNC_FLAG << 1;  // This is a synthetic builtin function (isInitialized or isBound)
    public static final long FUNC_POINTER_MAKE      = FIRST_VISAGE_FUNC_FLAG << 2;  // This is the special Pointer.make() function
    public static final long FUNC_SYNTH_LOCAL_DOIT  = FIRST_VISAGE_FUNC_FLAG << 3;  // Synthetic function holding a local context
    
    // Class flags -- reuse same bits as VARUSE* flags
    private static final long FIRST_VISAGE_CLASS_FLAG    = LAST_VISAGE_MOD_FLAG << 1;
    public static final long VISAGE_CLASS                = FIRST_VISAGE_CLASS_FLAG << 1;  // Visage class
    public static final long CLASS_HAS_INIT_BLOCK    = FIRST_VISAGE_CLASS_FLAG << 2;  // there is an init block on the class
    public static final long VISAGE_BOUND_FUNCTION_CLASS = FIRST_VISAGE_CLASS_FLAG << 3;  // This is a local class that implements bound functions

    public static final long VisageAccessFlags = PUBLIC | PROTECTED | PRIVATE | SCRIPT_PRIVATE;
    public static final long VisageExplicitAccessFlags = PUBLIC | PROTECTED | PRIVATE | PACKAGE_ACCESS;
    
    public static final long VisageUserFlags            = VisageExplicitAccessFlags | MIXIN | STATIC | ABSTRACT | BOUND | OVERRIDE | DEFAULT | PUBLIC_READ | PUBLIC_INIT;
    public static final long VisageLocalVarFlags        = PARAMETER;
    public static final long VisageInstanceVarFlags     = VisageExplicitAccessFlags | DEFAULT | PUBLIC_READ | PUBLIC_INIT;
    public static final long VisageAllInstanceVarFlags  = VisageExplicitAccessFlags | DEFAULT | PUBLIC_READ | PUBLIC_INIT | SCRIPT_PRIVATE;
    public static final long VisageScriptVarFlags       = VisageExplicitAccessFlags | STATIC | PUBLIC_READ;
    public static final long VisageMemberDefFlags       = VisageExplicitAccessFlags | STATIC | PUBLIC_READ;
    public static final long VisageFunctionFlags        = VisageExplicitAccessFlags | ABSTRACT | BOUND | OVERRIDE;
    public static final long VisageScriptFunctionFlags  = VisageExplicitAccessFlags | STATIC | BOUND;
    public static final long VisageClassFlags           = VisageExplicitAccessFlags | MIXIN | STATIC | ABSTRACT;
}
