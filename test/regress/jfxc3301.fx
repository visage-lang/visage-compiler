/**
 * JFXC-3301 :  unexpected type found: Integer, required: Object[].
 *
 * @test
 * @compile jfxc3301.fx
 */

class P{} 

function getPath(ps:P[], index:Integer):P[]{ ps } 
function sortPath(ps:P[]):P[]{ ps } 


var path = [P{}, P{}]; 

// The following line used to result in compiler error:
// "unexpected type found: Integer, required: Object[]"

var sort = sortPath(getPath(path, 3))[0..1]; 
