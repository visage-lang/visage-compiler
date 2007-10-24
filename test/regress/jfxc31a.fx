/*
 * Regression test: translation within if
 *
 * @test
 */

class Cursor {}

class FXCursor {
    protected attribute awtCursor:  Cursor;
    public operation getCursor():  Cursor  {
    	if (awtCursor == null) then {
       	     awtCursor = createCursor();
   	 };
    	return awtCursor;
	}
    protected operation createCursor(): Cursor {null }
}
