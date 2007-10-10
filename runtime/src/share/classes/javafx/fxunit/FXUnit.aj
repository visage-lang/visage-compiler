public aspect FXUnit {
      declare parents: javafx.fxunit.FXTestCase+ && !javafx.fxunit.FXTestCase extends junit.framework.TestCase;
}
