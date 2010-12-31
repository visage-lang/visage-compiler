/*
 * Copyright (c) 2010-2011, Visage Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name Visage nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * Feature test #28 - length literals
 *
 * @author Stephen Chin <steveonjava@gmail.com>
 * @test
 * @run
 */
import javafx.lang.Length;
import javafx.lang.LengthUnit;

println("Zero = {Length.ZERO}");
var l = Length.valueOf(50.0, LengthUnit.DENSITY_INDEPENDENT_PIXEL);
println("50dp = {l.toString()}");
l = 500mm;
println("500mm = {%#s l}");
l = 5cm;
println("5cm = {%#s l} or {%.0f l.toMillimeters()}mm");
l = 1.5cm;
println("1.5cm = {%#s l} or {%.2f l.toInches()}in");
l = 2%;
println("2%% = {l}");
l = 5cm + 7mm;
println("5cm + 7mm = {%#s l}");
l = 1in + 25cm + 15mm;
println("1in + 25cm + 15mm = {%#s l}");
l = 120px;
println("120px = {l.toDensityIndependentPixels(1)}dp");

println("1cm + 1mm = {%#s 1cm + 1mm}");
println("1cm - 1mm = {%#s 1cm - 1mm}");
println("50dp * 2 = {50dp * 2}");
println("50dp / 2 = {50dp / 2}");
println("5cm < 5mm = {5cm < 5mm}");
println("5cm <= 5mm = {5cm <= 5mm}");
println("5cm > 5mm = {5cm > 5mm}");
println("5cm >= 5mm = {5cm >= 5mm}");
println("5cm == 5mm = {5cm == 5mm}");
println("5cm != 5mm = {5cm != 5mm}");
