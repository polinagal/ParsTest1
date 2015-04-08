/*
 * $Id: DepsConfigManager.java 19739 2010-12-23 10:17:27Z zakharov $
 */
package s2a.util.config;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;

/**
 * Configuration Manager.
 *
 * @author Marat Kh. Akhin
 */
public final class DepsConfigManager extends AbstractConfigManager {
    //--------------------------------------------------------------------------
    @Option(name = "--theoryName",
            aliases = "-tm",
            metaVar = "THEORY_NAME",
            usage = "theory which is used to prove facts: smt, mgp or bpr")
    String theoryName = "mgp";

    public String getTheoryName() {
        return theoryName;
    }

    public void setTheoryName(final String theoryName) {
        this.theoryName = theoryName;
    }

    //--------------------------------------------------------------------------
    @Option(name = "--theoryDeep",
            aliases = "-td",
            metaVar = "THEORY_DEEP",
            usage = "how deep prover can go (number of recursive rules used, 0 means infinite)")
    int theoryDeep = 10;

    public int getTheoryDeep() {
        return theoryDeep;
    }

    public void setTheoryDeep(final int theoryDeep) {
        this.theoryDeep = theoryDeep;
    }

    //--------------------------------------------------------------------------

    static {
        instance = new DepsConfigManager();
    }

    DepsConfigManager() {
        super();
    }

    @Override
    public void reset() {
        reset(new DepsConfigManager());
    }

    public static DepsConfigManager getInstance() {
        return (DepsConfigManager)instance;
    }

    @Override
    protected void checkArgs() throws CmdLineException {

    }
}
