/*
 * $Id:$
 */

package s2a.predicates.api;

import java.util.LinkedList;
import java.util.List;

import s2a.predicates.impl.PredicateFactory;

/**
 * <p>Здесь перечисляются поддерживаемые типы предикатов.
 * Список пока был составлен на базе PSSV-статьи.</p>
 *
 * <p>Предикаты, по-видимому, различаются по следующим признакам:</p>
 * <ul>
 * <li>количество связанных объектов</li>
 * <li>требования к связанным объектам</li>
 * </ul>
 *
 * <p>Составной объект, если он участвует в предикате, всегда идет первым.
 * Некоторые предикаты состоят только из значений, другие - только из
 * других предикатов, третьи включают один составной объект, остальные значения.
 * Многие предикаты не допускают константных значений, или допускают лишь
 * одну константу - она идет последней.</p>
 *
 * <p>Далее в комментариях к конкретным предикатам
 * используются следующие обозначения:</p>
 * <ul>
 * <li>a, b, c - объекты, имеющие конкретное значение</li>
 * <li>p, q - объекты-предикаты</li>
 * <li>x, y - составные объекты</li>
 * <li>r - ресурс</li>
 * <li>s - состояние ресурса</li>
 * </ul>
 * @author Mikhail Glukhikh
 */
public enum PredicateType {

    // Знания общего плана
    /** всегда истинный предикат */
    TRUTH(0) {
        protected void checkArgumentTypes(final List<PredicateObject> args) {}
    },
    /** всегда ложный предикат */
    FALSEHOOD(0) {
        protected void checkArgumentTypes(final List<PredicateObject> args) {}
    },
    /** a = false (или 0, или NULL) */
    ZERO(1) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            // FALSE(0) ==> TRUTH, FALSE(c!=0) ==> FALSEHOOD
            if (args.get(0) instanceof SimpleConstantObject) {
                final SimpleConstantObject co = (SimpleConstantObject)args.get(0);
                if (co.getConstantValue() != 0) {
                    return FALSE_PRED;
                } else {
                    return TRUE_PRED;
                }
            }
            return null;
        }
    },
    /** a = true (не 0) */
    NONZERO(1) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
            //checkNoConstants(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            // TRUE(c!=0) ==> TRUTH, TRUE(0) ==> FALSEHOOD
            if (args.get(0) instanceof SimpleConstantObject) {
                final SimpleConstantObject co = (SimpleConstantObject)args.get(0);
                if (co.getConstantValue() != 0) {
                    return TRUE_PRED;
                } else {
                    return FALSE_PRED;
                }
            }
            return null;
        }
   },
    /** a initialized */
    INIT(1) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
            checkNoConstants(args);
        }
    },
    /** a has invalid value */
    INVALID(1) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
            checkNoConstants(args);
        }
    },
    /** a has valid value */
    VALID(1) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
            checkNoConstants(args);
        }
    },
    /** a = b */
    EQUALS(2) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            // EQUALS(const, const) ==> TRUTH, EQUALS(const1, const2) ==> FALSEHOOD
            if (args.get(0) instanceof SimpleConstantObject &&
                args.get(1) instanceof SimpleConstantObject) {
                final SimpleConstantObject co0 = (SimpleConstantObject)args.get(0);
                final SimpleConstantObject co1 = (SimpleConstantObject)args.get(1);
                if (co0.getConstantValue() != co1.getConstantValue()) {
                    return FALSE_PRED;
                } else {
                    return TRUE_PRED;
                }
            }
            // EQUALS(a, a) ==> TRUTH
            if (args.get(0).equals(args.get(1))) {
                return TRUE_PRED;
            }
            return null;
        }
    },

    // Арифметика
    /** a = -b */
    NEG(2) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
        }

        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            // NEG(const, -const) ==> TRUTH, NEG(const1, const2) ==> FALSEHOOD (const1 != -const2)
            if (args.get(0) instanceof SimpleConstantObject &&
                args.get(1) instanceof SimpleConstantObject) {
                final SimpleConstantObject co0 = (SimpleConstantObject)args.get(0);
                final SimpleConstantObject co1 = (SimpleConstantObject)args.get(1);
                if (co0.getConstantValue() != -co1.getConstantValue()) {
                    return FALSE_PRED;
                } else {
                    return TRUE_PRED;
                }
            }
            return null;
        }
    },
    /** a = b + c */
    SUM(3) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            // SUM (a, const1, const2) ==> EQUALS(a, const1+const2)
            if (args.get(1) instanceof SimpleConstantObject &&
                args.get(2) instanceof SimpleConstantObject) {
                final SimpleConstantObject co1 = (SimpleConstantObject)args.get(1);
                final SimpleConstantObject co2 = (SimpleConstantObject)args.get(2);
                final long sum = co1.getConstantValue() + co2.getConstantValue();
                if (args.get(0) instanceof SimpleConstantObject) {
                    final SimpleConstantObject co0 = (SimpleConstantObject)args.get(0);
                    if (co0.getConstantValue() != sum) {
                        return FALSE_PRED;
                    } else {
                        return TRUE_PRED;
                    }
                } else {
                    return factory.createPredicate(EQUALS, args.get(0),
                            factory.createIntegerConstantObject(sum, co1.getValueSize()));
                }
            }
            return null;
        }
    },
    /** a = b - c */
    DIFF(3) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            // DIFF (a, const1, const2) ==> EQUALS(a, const1-const2)
            if (args.get(1) instanceof SimpleConstantObject &&
                args.get(2) instanceof SimpleConstantObject) {
                final SimpleConstantObject co1 = (SimpleConstantObject)args.get(1);
                final SimpleConstantObject co2 = (SimpleConstantObject)args.get(2);
                final long diff = co1.getConstantValue() - co2.getConstantValue();
                if (args.get(0) instanceof SimpleConstantObject) {
                    final SimpleConstantObject co0 = (SimpleConstantObject)args.get(0);
                    if (co0.getConstantValue() != diff) {
                        return FALSE_PRED;
                    } else {
                        return TRUE_PRED;
                    }
                } else {
                    return factory.createPredicate(EQUALS, args.get(0),
                            factory.createIntegerConstantObject(diff, co1.getValueSize()));
                }
            }
            return null;
        }
    },
    /** a = b * c */
    PROD(3) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            // PROD (a, const1, const2) ==> EQUALS(a, const1*const2)
            if (args.get(1) instanceof SimpleConstantObject &&
                args.get(2) instanceof SimpleConstantObject) {
                final SimpleConstantObject co1 = (SimpleConstantObject)args.get(1);
                final SimpleConstantObject co2 = (SimpleConstantObject)args.get(2);
                final long prod = co1.getConstantValue() * co2.getConstantValue();
                if (args.get(0) instanceof SimpleConstantObject) {
                    final SimpleConstantObject co0 = (SimpleConstantObject)args.get(0);
                    if (co0.getConstantValue() != prod) {
                        return FALSE_PRED;
                    } else {
                        return TRUE_PRED;
                    }
                } else {
                    return factory.createPredicate(EQUALS, args.get(0),
                            factory.createIntegerConstantObject(prod, co1.getValueSize()));
                }
            }
            return null;
        }
    },
    /** a = b / c */
    QUOT(3) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            // QUOT (a, const1, const2) ==> EQUALS(a, const1/const2)
            if (args.get(1) instanceof SimpleConstantObject &&
                args.get(2) instanceof SimpleConstantObject) {
                final SimpleConstantObject co1 = (SimpleConstantObject)args.get(1);
                final SimpleConstantObject co2 = (SimpleConstantObject)args.get(2);
                // Protection from division by zero
                if (co2.getConstantValue()==0)
                    return FALSE_PRED;
                final long quot = co1.getConstantValue() / co2.getConstantValue();
                if (args.get(0) instanceof SimpleConstantObject) {
                    final SimpleConstantObject co0 = (SimpleConstantObject)args.get(0);
                    if (co0.getConstantValue() != quot) {
                        return FALSE_PRED;
                    } else {
                        return TRUE_PRED;
                    }
                } else {
                    return factory.createPredicate(EQUALS, args.get(0),
                            factory.createIntegerConstantObject(quot, co1.getValueSize()));
                }
            }
            return null;
        }
    },
    /* остаток от деления 
     * rem(res,a,b) <==> res = a % b
     */
    REM(3){
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
        }
    },
    /** unsigned a = signed b */
    UNSIGNED(2) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
            checkNoConstants(args);
        }
    },

    // Логика
    /** a = b or c */
    OR(3) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
            checkNoConstants(args);
        }
    },
    /** a = b and c */
    AND(3) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
            checkNoConstants(args);
        }
    },
    /* multi_and(a,b,c,..,n) <==> a and b and c and ... and n */
    AND_COMPL(-1){
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            if(args.size() < 2){
                throw new AssertionError("Предикат требует хотя бы два аргумента");
            }
        }
    },
    /** a = not b */
    NOT(2) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
            checkNoConstants(args);
        }
    },
    // Сравнения
    /** a &lt; b */
    LESS(2) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            if (args.get(0) instanceof SimpleConstantObject &&
                args.get(1) instanceof SimpleConstantObject) {
                final SimpleConstantObject co0 = (SimpleConstantObject)args.get(0);
                final SimpleConstantObject co1 = (SimpleConstantObject)args.get(1);
                if (co0.getConstantValue() >= co1.getConstantValue()) {
                    return FALSE_PRED;
                } else {
                    return TRUE_PRED;
                }
            }
            return null;
        }
    },
    /** a &lt;= b */
    LESS_EQUALS(2) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            if (args.get(0) instanceof SimpleConstantObject &&
                args.get(1) instanceof SimpleConstantObject) {
                final SimpleConstantObject co0 = (SimpleConstantObject)args.get(0);
                final SimpleConstantObject co1 = (SimpleConstantObject)args.get(1);
                if (co0.getConstantValue() > co1.getConstantValue()) {
                    return FALSE_PRED;
                } else {
                    return TRUE_PRED;
                }
            }
            return null;
        }
    },
    /** a &gt; b */
    GREATER(2) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            if (args.get(0) instanceof SimpleConstantObject &&
                args.get(1) instanceof SimpleConstantObject) {
                final SimpleConstantObject co0 = (SimpleConstantObject)args.get(0);
                final SimpleConstantObject co1 = (SimpleConstantObject)args.get(1);
                if (co0.getConstantValue() <= co1.getConstantValue()) {
                    return FALSE_PRED;
                } else {
                    return TRUE_PRED;
                }
            }
            return null;
        }
    },
    /** a &gt;= b */
    GREATER_EQUALS(2) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            if (args.get(0) instanceof SimpleConstantObject &&
                args.get(1) instanceof SimpleConstantObject) {
                final SimpleConstantObject co0 = (SimpleConstantObject)args.get(0);
                final SimpleConstantObject co1 = (SimpleConstantObject)args.get(1);
                if (co0.getConstantValue() < co1.getConstantValue()) {
                    return FALSE_PRED;
                } else {
                    return TRUE_PRED;
                }
            }
            return null;
        }
    },
    /** a != b */
    NOT_EQUALS(2) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            if (args.get(0) instanceof SimpleConstantObject &&
                args.get(1) instanceof SimpleConstantObject) {
                final SimpleConstantObject co0 = (SimpleConstantObject)args.get(0);
                final SimpleConstantObject co1 = (SimpleConstantObject)args.get(1);
                if (co0.getConstantValue() == co1.getConstantValue()) {
                    return FALSE_PRED;
                } else {
                    return TRUE_PRED;
                }
            }
            return null;
        }
    },
    /** a in [b...c] */
    IN_RANGE(3){

        @Override
        protected void checkArgumentTypes(List<PredicateObject> args) throws PredicateCreateException {
            checkValues(args);
        }

    },
    
    // Указатели
    /* a = &x + b, запись PTR(x, a, b) */
    PTR(3,true) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            //checkComplex(args);
            if (args.get(1) instanceof ConstantObject)
                throw new PredicateCreateException(
                        "1-й аргумент предиката не может быть константой: " +
                        this.toString() + ": " + args);
        }
    },
    /** Argument is an invalid pointer */
    INCORRECT_PTR(1){
         protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            if (args.get(0) instanceof ConstantObject)
                throw new PredicateCreateException(
                        "0-й аргумент предиката не может быть константой: " +
                        this.toString() + ": " + args);
        }
    },
    /** Argument is a valid pointer */
    CORRECT_PTR(1){
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            if (args.get(0) instanceof ConstantObject)
                throw new PredicateCreateException(
                        "0-й аргумент предиката не может быть константой: " +
                                this.toString() + ": " + args);
        }
    },
    /** read by dereference a = *b */
    DEREFFROM(2) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkValues(args);
            //checkNoConstants(args);
        }
    },

    // Операции над предикатами
    /** not p is true */
    OPPOS(1) {
        @Override
        public boolean higherOrder() {
            return true;
        }
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkPredicates(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            // OPPOS(TRUTH) ==> FALSEHOOD
            // OPPOS(FALSEHOOD) ==> TRUTH
            // OPPOS(TRUE(p)) ==> FALSE(p)
            // OPPOS(FALSE(p)) ==> TRUE(p)
            // OPPOS(LESS) ==> GREATER_EQ и другие сравнения
            final Predicate arg = (Predicate)args.get(0);
            if (TRUE_PRED.equals(arg)) {
                return FALSE_PRED;
            } else if (FALSE_PRED.equals(args)) {
                return TRUE_PRED;
            } else if (arg.getType() == OPPOS) {
                return (Predicate)arg.getArguments().get(0);
            } else if (arg.getType()== NONZERO) {
                return factory.createPredicate(ZERO, arg.getArguments());
            } else if (arg.getType()== ZERO) {
                return factory.createPredicate(NONZERO, arg.getArguments());
            } else if (arg.getType()==LESS) {
                return factory.createPredicate(GREATER_EQUALS, arg.getArguments());
            } else if (arg.getType()==SIZEOF_LESS) {
                return factory.createPredicate(SIZEOF_GREATER_EQUALS, arg.getArguments());
            } else if (arg.getType()==LESS_EQUALS) {
                return factory.createPredicate(GREATER, arg.getArguments());
            } else if (arg.getType()==GREATER) {
                return factory.createPredicate(LESS_EQUALS, arg.getArguments());
            } else if (arg.getType()==GREATER_EQUALS) {
                return factory.createPredicate(LESS, arg.getArguments());
            } else if (arg.getType()==SIZEOF_GREATER_EQUALS) {
                return factory.createPredicate(SIZEOF_LESS, arg.getArguments());
            } else if (arg.getType()==EQUALS) {
                return factory.createPredicate(NOT_EQUALS, arg.getArguments());
            } else if (arg.getType()==NOT_EQUALS) {
                return factory.createPredicate(EQUALS, arg.getArguments());
            } else if(arg.getType()==INVALID){
                return factory.createPredicate(VALID, arg.getArguments());
            } else if(arg.getType()==VALID){
                return factory.createPredicate(INVALID, arg.getArguments());
            } else if (arg.getType()==INCORRECT_PTR) {
                return factory.createPredicate(CORRECT_PTR, arg.getArguments());
            } else if (arg.getType()==CORRECT_PTR) {
                return factory.createPredicate(INCORRECT_PTR, arg.getArguments());
            }
            return null;
        }
    },
    /* p <==> q */
    EQUIV(2) {
        @Override
        public boolean higherOrder() {
            return true;
        }
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkPredicates(args);
        }
        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            if (TRUE_PRED.equals(args.get(0))) {
                if (TRUE_PRED.equals(args.get(1))) {
                    return TRUE_PRED;
                } else if (FALSE_PRED.equals(args.get(1))) {
                    return FALSE_PRED;
                }
                return (Predicate)args.get(1);
            } else if (FALSE_PRED.equals(args.get(0))) {
                if (TRUE_PRED.equals(args.get(1))) {
                    return FALSE_PRED;
                } else if (FALSE_PRED.equals(args.get(1))) {
                    return TRUE_PRED;
                }
                return factory.createPredicate(OPPOS, args.get(1));
            } else if (TRUE_PRED.equals(args.get(1))) {
                return (Predicate)args.get(0);
            } else if (FALSE_PRED.equals(args.get(1))) {
                return factory.createPredicate(OPPOS, args.get(0));
            }
            return null;
        }
    },

	/** a: p1(a) or p2(a) or ... or pN(a) is true */
    ONEOF(1) {
        @Override
        public boolean higherOrder() {
            return true;
        }
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
//            if (args.size() < 3) {
//                throw new PredicateCreateException("Слишком мало аргументов " +
//                        "у ONE_OF: " + args);
//            }
            final PredicateObject first = args.get(0);
//            if (!(first instanceof ValueObject)) {
//                throw new PredicateCreateException("Первый аргумент ONE_OF " +
//                        "должен быть значением: " + args);
//            }
            if (first instanceof ConstantObject) {
                throw new PredicateCreateException("Первый аргумент ONE_OF " +
                        "не может быть константой: " + args);
            }
            for (PredicateObject arg: args.subList(1, args.size())) {
                if (!(arg instanceof Predicate)) {
                    throw new PredicateCreateException(
                            "Аргументы ONE_OF (кроме первого) должны быть предикатами: " +
                            this.toString() + ": " + args);
                } else {
                    final Predicate p = (Predicate)arg;
                    if (p.getArguments().isEmpty()) {
                        throw new PredicateCreateException(
                                "Не допускаются пустые предикаты в ONE_OF: " + args);
                    }
                    if (!p.getArguments().get(0).equals(first)) {
                        throw new PredicateCreateException(
                                "Все предикаты в ONE_OF должны описывать одно значение: " + args);
                    }
                }
            }
        }

        @Override
        public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
            if (args.size() > 1 || !(args.iterator().next() instanceof ListObject)) {
                return factory.createPredicate(ONEOF, factory.createListObject(args));
            }
            // Раскрываем вложенные ONE_OF
            final ListObject listArg = (ListObject)args.iterator().next();
            final List<PredicateObject> outerArgs = new LinkedList<PredicateObject>();
            final List<PredicateObject> innerArgs = new LinkedList<PredicateObject>();
            for (Object aListArg : listArg) {
                final Predicate pred = (Predicate)aListArg;
                if (pred.getType() == ONEOF) {
                    final ListObject listInner = (ListObject) pred.getArguments().iterator().next();
                    for (PredicateObject innerObj : listInner)
                        innerArgs.add(innerObj);
                } else if (pred.getType() == TRUTH) {
                    return TRUE_PRED;
                } else {
                    outerArgs.add(pred);
                }
            }
            if (innerArgs.isEmpty()) {
                return null;
            } else {
                outerArgs.addAll(innerArgs);
                return factory.createPredicate(ONEOF, outerArgs);
            }
        }
    },
    
    // arrayset(Array, Element, Shift) 
    //*(Array + Shift) = Element
    ARRAYSET(3,true){
        @Override
        protected void checkArgumentTypes(List<PredicateObject> args) throws PredicateCreateException {
            //throw new UnsupportedOperationException("Not supported yet.");
        }   
    },
    URETRACT(1,false){

        @Override
        protected void checkArgumentTypes(List<PredicateObject> args) throws PredicateCreateException {
            //throw new UnsupportedOperationException("Not supported yet.");
        }     
    },

    // Составные объекты
    /** x exists (changed to SIZE_OF) */
//    EXISTS(1) {
//        protected void checkArgumentTypes(final List<PredicateObject> args)
//                throws PredicateCreateException {
//            checkComplex(args);
//        }
//        @Override
//        public String toString() {
//            return "EXISTS";
//        }
//    },
    /** a = sizeof(x) размер объекта в байтах, SIZE_OF(x, a) */
    SIZEOF(2,true) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            // Temporary commented (complex quantifier required)
//            checkComplex(args);
        }
    },
    /* sizeof_less(obj,num) <==> sizeof(obj,size) && num < size */
    SIZEOF_LESS(2){
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            // Temporary commented (complex quantifier required)
//            checkComplex(args);
        }
    },
    /* sizeof_greater_equals(obj,num) <==> sizeof(obj,size) && num >= size */
    SIZEOF_GREATER_EQUALS(2){
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            // Temporary commented (complex quantifier required)
//            checkComplex(args);
        }
    },

    VAR(1){
        @Override
        protected void checkArgumentTypes(List<PredicateObject> args) throws PredicateCreateException {
        }        
    },
    /** a = init number of(x) число инициализированных элементов массива,
        может быть, нужно смещение, INIT_OF(x, a) */
    INIT_OF(2) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkComplex(args);
        }
    },
    /** a = lengthof(x) длина строки в символах,
        может быть, нужно смещение, LENGTH_OF(x, a) */
    LENGTH_OF(2) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            checkComplex(args);
        }
    },

    /** unknown type for quantifiers */
    UNKNOWN(0) {
        protected void checkArgumentTypes(final List<PredicateObject> args)
                throws PredicateCreateException {
            if (!args.isEmpty()) {
                throw new PredicateCreateException("У предиката не должно быть аргументов");
            }
        }
    };
 
    static private final PredicateFactory factory = PredicateFactory.getInstance();

    static public final Predicate TRUE_PRED, FALSE_PRED;

    static {
        try {
            TRUE_PRED = factory.createPredicate(TRUTH);
            FALSE_PRED = factory.createPredicate(FALSEHOOD);
        } catch (PredicateCreateException ex) {
            throw new AssertionError(ex);
        }
    }

    private final int argsNumber;
    private final boolean isFact;

    public boolean isFact(){
	    return isFact;
    }
    protected void checkNoConstants(final List<PredicateObject> args)
            throws PredicateCreateException {
        for (PredicateObject arg: args) {
            if (arg instanceof ConstantObject)
                throw new PredicateCreateException(
                        "Аргументы предиката не могут быть константами: " + this.toString() + ": " + args);
        }
    }

    protected void checkOnlyLastConstant(final List<PredicateObject> args)
            throws PredicateCreateException {
        for (PredicateObject arg: args.subList(0, args.size()-1)) {
            if (arg instanceof ConstantObject)
                throw new PredicateCreateException(
                        "Аргументы предиката не могут быть константами, "
                        + "за исключением последнего: " + this.toString() + ": " + args);
        }
    }

    protected void checkValues(final List<PredicateObject> args)
            throws PredicateCreateException {
        for (PredicateObject arg: args) {
            //if (!(arg instanceof ValueObject))
                //throw new PredicateCreateException(
                //        "Аргументы предиката должны быть значениями: " + this.toString() + ": " + args);
        }
    }

    protected void checkPredicates(final List<PredicateObject> args)
            throws PredicateCreateException {
        for (PredicateObject arg: args) {
            if (!(arg instanceof Predicate))
                throw new PredicateCreateException(
                        "Аргументы предиката должны быть предикатами: " + this.toString() + ": " + args);
        }
    }

    protected void checkComplex(final List<PredicateObject> args)
            throws PredicateCreateException {
        boolean first = true;
        for (PredicateObject arg: args) {
            if (first) {
                first = false;
                if (!(arg instanceof ComplexObject))
                    throw new PredicateCreateException(
                            "1-й аргумент предиката должен быть составным: " + this.toString() + ": " + args);
            } else {
                //if (!(arg instanceof ValueObject))
                   // throw new PredicateCreateException(
                   //         "2-й и следующие аргументы предиката должны быть значениями: " + this.toString() + ": " + args);
            }
        }
    }

    abstract protected void checkArgumentTypes(final List<PredicateObject> args)
            throws PredicateCreateException;

    /**
     * Конструктор
     * @param args число аргументов предиката, -1 означает "любое > 1"
     */
    PredicateType(final int args) {
        this.argsNumber = args;
    	this.isFact = false;
    }

    PredicateType(final int args, boolean isFact){
	    this.argsNumber = args;
	    this.isFact = isFact;
    }

    /**
     *
     * @return число аргументов предиката
     */
    public int getArgsNumber() {
        return argsNumber;
    }

    /**
     *
     * @return true, если предикат высшего порядка, то есть
     * может иметь аргументами предикаты
     */
    public boolean higherOrder() {
        return false;
    }

    /**
     * Эта функция должна проверить
     * типы аргументов предиката и вызвать
     * assert, если что-то не так
     * @param args аргументы предиката
     */
    public void checkArguments(final List<PredicateObject> args)
            throws PredicateCreateException {
        if (argsNumber==-1 && args.size()==1)
            throw new PredicateCreateException(
                    "Предикат должен иметь хотя бы 2 аргумента: " + this.toString() + ": " + args);
        if (argsNumber > 0 && args.size() != argsNumber)
            throw new PredicateCreateException(
                    "Неверное число аргументов предиката - должно быть " + argsNumber + ": " + this.toString() + ": " + args);
        checkArgumentTypes(args);
    }

    /**
     * Эта функция должна упростить предикат, если это возможно;
     * как правило, выполняются все возможные операции над константами
     * @param args аргументы предиката
     * @return упрощенный предикат, или null, если упрощение невозможно
     * @throws PredicateCreateException если не удается создать
     * упрощенный предикат
     */
    public Predicate simplify(final List<PredicateObject> args)
            throws PredicateCreateException {
        return null;
    }

    /**
     * Преобразование в строку
     * @return строка с именем типа
     */
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
