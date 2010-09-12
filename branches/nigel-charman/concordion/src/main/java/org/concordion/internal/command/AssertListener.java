package org.concordion.internal.command;

import java.util.EventListener;

public interface AssertListener extends EventListener {

    void successReported(AssertSuccessEvent event);
    
    void failureReported(AssertFailureEvent event);

}