package s2a.inference.api;

import s2a.inference.mgp.LogicianTheoryFactory;
import s2a.util.config.DepsConfigManager;

/**
 * Abstract factory for theories creation
 */
public abstract class AbstractTheoryFactory {
    private static AbstractTheoryFactory mgpInstance = LogicianTheoryFactory.instance;

    public static AbstractTheoryFactory getInstance() {
        final String theoryName = DepsConfigManager.getInstance().getTheoryName();
        if ("mgp".equalsIgnoreCase(theoryName))
            return mgpInstance;
        return null;
    }

    abstract public Theory createTheory();
}
