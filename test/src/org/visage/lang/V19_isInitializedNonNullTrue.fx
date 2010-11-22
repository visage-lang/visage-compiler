/*
 * Copyright 2010 Visage Project
 *
 * This file is part of Visage. Visage is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * Visage is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * JavaFX source file reproducing the issue detailed in Visage Issue 19
 * (http://code.google.com/p/visage/issues/detail?id=19).
 *
 * @author J.H. Kuperus
 */

class TestClass {
  public var name: String
}


def test:TestClass = TestClass {name:"Test"};

// Should return true
isInitialized(test.name);