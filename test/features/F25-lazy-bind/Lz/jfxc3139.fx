/*
 * @test
 * 
 */

// When this isuue will be fixed, please, update test
// features/F25-lazy-bind/Lz/lzIfThenElseGlobal.fx

function fCharacter(x : Character) : Character{
    return x;
}

var valueCharacter : Character = 64 as Character;
def bindedCharacter : Character = bind 
    if(fCharacter(valueCharacter) != 64) then 62 else 64;
