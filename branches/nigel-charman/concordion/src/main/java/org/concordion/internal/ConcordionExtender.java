package org.concordion.internal;

import org.concordion.api.Command;
import org.concordion.api.Resource;
import org.concordion.internal.command.AssertEqualsListener;
import org.concordion.internal.command.AssertFalseListener;
import org.concordion.internal.command.AssertTrueListener;
import org.concordion.internal.command.ExecuteListener;
import org.concordion.internal.command.SpecificationProcessingListener;
import org.concordion.internal.command.ThrowableCaughtListener;
import org.concordion.internal.command.VerifyRowsListener;

public interface ConcordionExtender {
    ConcordionExtender withCommand(String namespaceURI, String commandName, Command command);
    ConcordionExtender withAssertEqualsListener(AssertEqualsListener listener);
    ConcordionExtender withAssertTrueListener(AssertTrueListener listener);
    ConcordionExtender withAssertFalseListener(AssertFalseListener listener);
    ConcordionExtender withDocumentParsingListener(DocumentParsingListener listener);
    ConcordionExtender withExecuteListener(ExecuteListener executeListener);
    ConcordionExtender withRunListener(RunListener listener);
    ConcordionExtender withSpecificationProcessingListener(SpecificationProcessingListener listener);
    ConcordionExtender withThrowableListener(ThrowableCaughtListener throwableListener);
    ConcordionExtender withVerifyRowsListener(VerifyRowsListener listener);
    ConcordionExtender withResource(String sourcePath, Resource targetResource);
    ConcordionExtender withCSS(String sourcePath, Resource targetResource);
}
