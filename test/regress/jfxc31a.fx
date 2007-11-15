/*
 * Regression test: translation within if
 *
 * @test
 */

class Cursor {}

class FXCursor {
    protected attribute awtCursor:  Cursor;
    public function getCursor():  Cursor  {
    	if (awtCursor == null) then {
       	     awtCursor = createCursor();
   	 };
    	return awtCursor;
	}
    protected function createCursor(): Cursor {null }
}
