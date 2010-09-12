package test.concordion.extension;

import org.concordion.internal.command.AssertEqualsListener;
import org.concordion.internal.command.AssertFailureEvent;
import org.concordion.internal.command.AssertFalseListener;
import org.concordion.internal.command.AssertSuccessEvent;
import org.concordion.internal.command.AssertTrueListener;

public class AssertLogger implements AssertEqualsListener, AssertTrueListener, AssertFalseListener {
    public void successReported(AssertSuccessEvent event) {
        System.out.println("Success '" + event.getElement().getText() + "'");
    }

    public void failureReported(AssertFailureEvent event) {
        System.out.println("Failure expected:'" + event.getExpected() + "' actual:'" + event.getActual() + "'");
    }
}