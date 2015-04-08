package s2a.inference.mgp;

import s2a.inference.api.AbstractTheoryFactory;
import s2a.inference.api.Theory;

/**
 * Factory for MGP theories creation
 */
public class LogicianTheoryFactory extends AbstractTheoryFactory {

    public static final LogicianTheoryFactory instance = new LogicianTheoryFactory();

    private LogicianTheoryFactory() {}

    @Override
    public Theory createTheory() {
        return new LogicianTheory();
    }

}
