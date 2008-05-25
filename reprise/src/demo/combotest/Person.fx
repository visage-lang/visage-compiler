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
package demo.combotest;

public class Person {

    private static attribute staticIdentifier: Integer = 0;

    private attribute identifier: Integer = (staticIdentifier++);

    public attribute firstName: String;

    public attribute lastName: String;

    public attribute male: Boolean;

    public attribute age: Integer;

    public function getIdentifier(): Integer {
        return identifier;
    }

    public static function createPersonSequence(): Person[] {
        return [
            Person {firstName: "Davey"  lastName: "Crocket"  male: true  age: 30},
            Person {firstName: "Shaun"  lastName: "Noble"    male: true  age: 30},
            Person {firstName: "Davey"  lastName: "Noble"    male: false age: 30},
            Person {firstName: "Matt"   lastName: "Dillon"   male: true  age:  8},
            Person {firstName: "Kerry"  lastName: "Henry"    male: true  age:  3},
            Person {firstName: "Brenda" lastName: "McMaster" male: false age: 28},
            Person {firstName: "Donna"  lastName: "Lacy"     male: false age: 26},
            Person {firstName: "Diana"  lastName: "Muslo"    male: false age: 29},
            Person {firstName: "Tyra"   lastName: "Happe"    male: false age: 60},
            Person {firstName: "Jose"   lastName: "Mendez"   male: true  age: 17},
            Person {firstName: "Nathan" lastName: "Godfrey"  male: true  age: 97},
            Person {firstName: "Taylor" lastName: "Hastings" male: false age: 33}
        ];
    }

    public function toString(): String {
        return "{firstName} {lastName}";
    }

}
