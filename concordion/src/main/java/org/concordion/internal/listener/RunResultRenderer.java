package org.concordion.internal.listener;

import org.concordion.api.command.RunFailureEvent;
import org.concordion.api.command.RunIgnoreEvent;
import org.concordion.api.command.RunListener;
import org.concordion.api.command.RunSuccessEvent;

public class RunResultRenderer extends ThrowableRenderer implements RunListener {

    public void successReported(RunSuccessEvent event) {
        event.getElement().addStyleClass("success").appendNonBreakingSpaceIfBlank();
    }

    public void failureReported(RunFailureEvent event) {
        event.getElement().addStyleClass("failure").appendNonBreakingSpaceIfBlank();
    }

    public void ignoredReported(RunIgnoreEvent event) {
        event.getElement().addStyleClass("ignored").appendNonBreakingSpaceIfBlank();
    }

}
