package simpleparser;

/**
 * 
 * @author Dante
 */

public enum PredicateType {

    OR(3),
    FALSE(1) ,
    ZERO(1),
    EQUALS(2);
 
    private final int argsNumber;
       
    PredicateType(final int args) {
        this.argsNumber = args;
    }

    public int getArgsNumber() {
        return argsNumber;
    }
}
