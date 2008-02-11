/**
 * Functional test:  To test different operators( Small stress test too).
 * @test
 * @run
 */
import java.lang.System;
import java.lang.Math;

function isPrime(n:Integer):Boolean {
    	return (sizeof (for(i in [2 .. Math.sqrt(n)] where n%i == 0) i) == 0);
}
function roundMe(src:Number, digits:Integer) {
	src *= Math.pow(10,digits);
	return Math.round(src)/Math.pow(10,digits);
}
function factors(n:Integer) {
    return for(i in [1 .. n/2] where n % i == 0) i;
}
function harmonics(n:Integer):Number {
	var flag:Number = 0;
	return roundMe(1 + ({for(i in [2..n] where n >= 2){ flag += roundMe((1.0/i),3);} flag;}),2);
}
function isArmstrong(n:Integer) {
	var flag:Integer=0;
	var num = n;
	while(n > 0) {
		flag += Math.pow(n%10,3);
		n /= 10;
	}
	return (flag==num);
} 
function main(n:Integer) {
	var primes:Integer[];
	var armstrongs:Integer[];
	var harmonicSeries:Number[];
	for(i in [2..n]) {
		if(isPrime(i)){ insert i into primes; }
		 else{ System.out.println("Factorts for {i} : " + factors(i)); }
		if(isArmstrong(i)){ insert i into armstrongs; }
		insert harmonics(i) into  harmonicSeries;
	}
	System.out.println("Pime numbers : " + primes);	
	System.out.println("Armstrongs numbers : " + armstrongs);
	System.out.println("Harmonic Serious : " + harmonicSeries);
}
main(1000);
