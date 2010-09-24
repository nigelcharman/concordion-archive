package org.concordion.api.extension;

import org.concordion.api.Command;
import org.concordion.api.Resource;
import org.concordion.api.command.AssertEqualsListener;
import org.concordion.api.command.AssertFalseListener;
import org.concordion.api.command.AssertTrueListener;
import org.concordion.api.command.ExecuteListener;
import org.concordion.api.command.RunListener;
import org.concordion.api.command.SpecificationProcessingListener;
import org.concordion.api.command.ThrowableCaughtListener;
import org.concordion.api.command.VerifyRowsListener;
import org.concordion.internal.DocumentParsingListener;

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
