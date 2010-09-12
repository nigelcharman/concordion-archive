package test.concordion.extension;

import org.concordion.api.Command;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.CommandCall;
import org.concordion.internal.ConcordionExtender;
import org.concordion.internal.ConcordionExtension;

public class CommandExtension implements ConcordionExtension {

    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withCommand("http://myorg.org/my/extension", "log", new Command() {
            
            public void verify(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
            }
            
            public void setUp(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
            }
            
            public void execute(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
                System.out.println(commandCall.getElement().getText());
            }
        });
    }
}
