package org.concordion.internal.extension;

import org.concordion.internal.ConcordionBuilder;

public interface ExtensionLoader {

    void addExtensions(Object fixture, ConcordionBuilder concordionBuilder);

}
