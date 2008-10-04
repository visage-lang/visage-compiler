/**
 * Regression test JFXC-910 : Binding Overhaul: bound object literals
 *
 * @test
 * @run
 */

import java.lang.System; 

class Owner {
  var name : String on replace { System.out.println("Update name: {name}") }
  init { System.out.println("Creating Owner name: {name}") }
  override function toString() : String {"Owner name: {name}"}
}

class Money {
  var dollars : Number on replace { System.out.println("Update dollars: {dollars}") }
  init { System.out.println("Creating Money dollars: {dollars}") }
  override function toString() : String {"Money dollars: {dollars}"}
}

class Combs {
  var count : Integer on replace { System.out.println("Update count: {count}") }
  init { System.out.println("Creating Combs count: {count}") }
  override function toString() : String {"Combs count: {count}"}
}

class Purse {
  var owner : Owner on replace { System.out.println("Update owner = {owner}") }
  var money : Money on replace { System.out.println("Update money = {money}") }
  var combs : Combs on replace { System.out.println("Update combs = {combs}") }
  init { System.out.println("Creating Purse") }
  override function toString() : String {"Purse {owner} {money} {combs}"}
}


System.out.println("--- Creating Q ---");

var QdaName = "Donna";
var QdaCash = 562.82;
var QdaCount = 1;

var pQ = bind Purse { 
	owner: Owner { name: QdaName }
	money: Money { dollars: QdaCash }
	combs: Combs { count: QdaCount }
}
System.out.println("{pQ}");

System.out.println("--- Changing QdaName ---");
QdaName = "Barb";
System.out.println("--- Changing QdaCash ---");
QdaCash = 14.27;
System.out.println("--- Changing QdaCount ---");
QdaCount = 3;

System.out.println("--- Creating R ---");

var RdaName = "Donna";
var RdaCash = 562.82;
var RdaCount = 1;

var pR = bind Purse { 
	owner: Owner { name: bind RdaName }
	money: Money { dollars: RdaCash }
	combs: Combs { count: RdaCount }
}

System.out.println("--- Changing RdaName ---");
RdaName = "Barb";
System.out.println("--- Changing RdaCash ---");
RdaCash = 14.27;
System.out.println("--- Changing RdaCount ---");
RdaCount = 3;

System.out.println("--- Creating S ---");

var SdaName = "Donna";
var SdaCash = 562.82;
var SdaCount = 1;

var pS = bind Purse { 
	owner: Owner { name: SdaName }
	money: Money { dollars: bind SdaCash }
	combs: Combs { count: SdaCount }
}

System.out.println("--- Changing SdaName ---");
SdaName = "Barb";
System.out.println("--- Changing SdaCash ---");
SdaCash = 14.27;
System.out.println("--- Changing SdaCount ---");
SdaCount = 3;

System.out.println("--- Creating T ---");

var TdaName = "Donna";
var TdaCash = 562.82;
var TdaCount = 1;

var pT = bind Purse { 
	owner: Owner { name: TdaName }
	money: Money { dollars: TdaCash }
	combs: Combs { count: bind TdaCount }
}

System.out.println("--- Changing TdaName ---");
TdaName = "Barb";
System.out.println("--- Changing TdaCash ---");
TdaCash = 14.27;
System.out.println("--- Changing TdaCount ---");
TdaCount = 3;

System.out.println("--- Creating U ---");

var UdaName = "Donna";
var UdaCash = 562.82;
var UdaCount = 1;

var pU = bind Purse { 
	owner: bind Owner { name: UdaName }
	money: Money { dollars: UdaCash }
	combs: Combs { count: UdaCount }
}

System.out.println("--- Changing UdaName ---");
UdaName = "Barb";
System.out.println("--- Changing UdaCash ---");
UdaCash = 14.27;
System.out.println("--- Changing UdaCount ---");
UdaCount = 3;

System.out.println("--- Creating V ---");

var VdaName = "Donna";
var VdaCash = 562.82;
var VdaCount = 1;

var pV = bind Purse { 
	owner: Owner { name: VdaName }
	money: bind Money { dollars: VdaCash }
	combs: Combs { count: VdaCount }
}

System.out.println("--- Changing VdaName ---");
VdaName = "Barb";
System.out.println("--- Changing VdaCash ---");
VdaCash = 14.27;
System.out.println("--- Changing VdaCount ---");
VdaCount = 3;

System.out.println("--- Creating W ---");

var WdaName = "Donna";
var WdaCash = 562.82;
var WdaCount = 1;

var pW = bind Purse { 
	owner: Owner { name: WdaName }
	money: Money { dollars: WdaCash }
	combs: bind Combs { count: WdaCount }
}

System.out.println("--- Changing WdaName ---");
WdaName = "Barb";
System.out.println("--- Changing WdaCash ---");
WdaCash = 14.27;
System.out.println("--- Changing WdaCount ---");
WdaCount = 3;

System.out.println("--- Creating X ---");

var XdaName = "Donna";
var XdaCash = 562.82;
var XdaCount = 1;

var pX = bind Purse { 
	owner: bind Owner { name: bind XdaName }
	money: Money { dollars: XdaCash }
	combs: Combs { count: XdaCount }
}

System.out.println("--- Changing XdaName ---");
XdaName = "Barb";
System.out.println("--- Changing XdaCash ---");
XdaCash = 14.27;
System.out.println("--- Changing XdaCount ---");
XdaCount = 3;

System.out.println("--- Creating Y ---");

var YdaName = "Donna";
var YdaCash = 562.82;
var YdaCount = 1;

var pY = bind Purse { 
	owner: Owner { name: YdaName }
	money: bind Money { dollars: bind YdaCash }
	combs: Combs { count: YdaCount }
}

System.out.println("--- Changing YdaName ---");
YdaName = "Barb";
System.out.println("--- Changing YdaCash ---");
YdaCash = 14.27;
System.out.println("--- Changing YdaCount ---");
YdaCount = 3;

System.out.println("--- Creating Z ---");

var ZdaName = "Donna";
var ZdaCash = 562.82;
var ZdaCount = 1;

var pZ = bind Purse { 
	owner: bind Owner { name: bind ZdaName }
	money: bind Money { dollars: bind ZdaCash }
	combs: bind Combs { count: bind ZdaCount }
}

System.out.println("--- Changing ZdaName ---");
ZdaName = "Barb";
System.out.println("--- Changing ZdaCash ---");
ZdaCash = 14.27;
System.out.println("--- Changing ZdaCount ---");
ZdaCount = 3;



