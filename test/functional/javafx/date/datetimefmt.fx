/*
 * Embedded formatting tests for javafx.date.DateTime
 *
 * @test
 * @run
 */

import javafx.date.DateTime;
import java.util.Date;

var date = new Date();
var time:Long = date.getTime();
var dt = DateTime { instant: time };

if (date.getTime() != dt.instant) {
    error("date.getTime()={date.getTime()}, dt.instant={dt.instant}");
}

var dateStr = "{ %tc dt}";
var dtStr = "{ %tc date }";
if (dateStr != dtStr) {
    error("tc: dateStr={dateStr}, dtStr={dtStr}");
}

dateStr = "{ %tFT%<tT%<tz date}";
dtStr = "{ %tFT%<tT%<tz dt}";
if (dateStr != dtStr) {
    error("FTz: dateStr={dateStr}, dtStr={dtStr}");
}

function error(msg:String):Void {
    throw new java.lang.RuntimeException(msg);
}
