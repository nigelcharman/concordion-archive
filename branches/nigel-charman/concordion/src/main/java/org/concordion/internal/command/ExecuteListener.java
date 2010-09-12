package org.concordion.internal.command;

import java.util.EventListener;


public interface ExecuteListener extends EventListener {

    void executeCompleted(ExecuteEvent e);

}
