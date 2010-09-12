package test.concordion.extension;

import org.concordion.internal.ConcordionExtender;
import org.concordion.internal.ConcordionExtension;
import org.concordion.internal.command.ExecuteListener;
import org.concordion.internal.command.VerifyRowsListener;

public class LoggingExtension implements ConcordionExtension {

    public void addTo(ConcordionExtender concordionExtender) {
        AssertLogger assertLogger = new AssertLogger();
        concordionExtender.withAssertEqualsListener(assertLogger);
        concordionExtender.withAssertTrueListener(assertLogger);
        concordionExtender.withAssertFalseListener(assertLogger);
        ExecuteListener executeListener = new ExecuteLogger();
        concordionExtender.withExecuteListener(executeListener);
        VerifyRowsListener verifyRowsListener = new VerifyRowsLogger();
        concordionExtender.withVerifyRowsListener(verifyRowsListener);
    }
}
