/*
 * $Id:$
 */

package s2a.inference.api;

import java.util.List;
import s2a.predicates.api.Predicate;

/**
 * <p>Здесь будет интерфейс, описывающий одно из правил логического вывода.</p>
 *
 * <p>Правило вывода - это нечто, представляющееся в форме
 * p ==> q или p <==> q, где p и q - два шаблона для предикатов.
 * Как правило, p является более сложным по структуре.
 * Кроме того, предпочтительнее иметь правила, не теряющие информацию -
 * то есть, правила эквивалентности, а не следствия. Дело в том,
 * что правила эквивалентности можно применять без раздумий -
 * например, в целях упрощения, а правила следствия можно
 * применять только для доказательства отсутствия дефектов.</p>
 *
 * <p>Мысли по поводу того, как делать вывод.
 * При выводе, при прочих равных нам предпочтительнее иметь
 * предикаты с константами, чем без них.
 * Например: EQUALS(a, b), EQUALS(b, const) <==> EQUALS(a, const), EQUALS(b, const).
 * Некоторые правила, типа коммутативности, имеет смысл применять, только когда
 * сразу за этим применяется другое правило.
 * Например: SUMM(a, b, c), ZERO(b) <==> SUMM(a, c, b), ZERO(b) <==>
 * EQUALS(a, c), ZERO(b).
 * Другие правила - явно упрощающие - можно применять в любом случае.
 * Это, например, подстановка константы.
 * </p>
 *
 * <p>Начинаться вывод может с нескольких ситуаций - мы рассматривали их
 * в PSSV-статье.
 * <ol>
 * <li>При интерпретации присваиваний, в начале вывода у нас есть
 * только что появившийся предикат. И вначале стоит попробовать
 * с чем-нибудь скомбинировать ЕГО, потом - тот предикат, который
 * появился в результате, и так далее. То есть, для нас нет смысла
 * всегда перебирать все имеющиеся предикаты.</li>
 * <li>Другая ситуация - это попытка что-нибудь доказать при
 * выявлении дефекта. Например, при разадресации *a у нас должны быть
 * предикаты вида POINTS_TO(x, a, b), SIZE_OF(x, s), и доказать нам нужно,
 * что LESS(b, s) и что LESS_EQ(0, b). В более примитивном варианте у нас
 * есть POINTS_TO(x, a, b) и нам надо доказать, что EXISTS(x).
 * Отталкиваться при доказательстве следует от того,
 * что мы знаем о b и x.</li>
 * <li>Наконец, третья, самая сложная ситуация, состоит в слиянии
 * двух множеств предикатов в фи-функции. В этом случае нужно, во-первых,
 * взять пересечение имеющихся предикатов, а во-вторых, рассмотреть пары
 * предикатов, касающиеся одних и тех же объектов, и попробовать их скомпоновать.
 * Например: (EXISTS(x), POINTS_TO(x, a, 0)) or (ZERO(a)) ==>
 * ONE_OF(ZERO(a), POINTS_TO(x, a, 0)), EQUIV(NONZERO(a), EXISTS(x)).</li>
 * </ol>
 * </p>
 *
 * <p>Далее в описании правил
 * используются следующие обозначения:</p>
 * <ul>
 * <li>a, b, c - объекты, имеющие конкретное значение</li>
 * <li>p, q - объекты-предикаты</li>
 * <li>x, y - составные объекты</li>
 * <li>r - ресурс</li>
 * <li>s - состояние ресурса</li>
 * <li>запятая означает AND</li>
 * </ul>
 *
 * <p>Какие правила вывода мы хотим (запятая означает AND,
 * правила подстановки годятся для любого предиката)?</p>
 * <ul>
 * <li>Comma: p, q ==> p; p, q ==> q</li>
 * <li>Comma #2: p, q <==> q, p</li>
 * <li>False def: EQUALS(a, 0) <==> ZERO(a)</li>
 * <li>False def #2: * ==> ZERO(0) (ZERO(0) можно хранить в люблом списке правил, не стирая)</li>
 * <li>True def: NOT_EQ(a, 0) <==> NONZERO(a)</li>
 * <li>True def conseq.: NOT_EQ(a, b), ZERO(b) <==> NONZERO(a), ZERO(b)</li>
 * <li>True or false: OPPOS(ZERO(a)) <==> NONZERO(a)</li>
 * <li>True or false #2: OPPOS(NONZERO(a)) <==> ZERO(a)</li>
 * <li>Double oppos: OPPOS(OPPOS(p)) <==> p</li>
 * <li>Init: ZERO(a) ==> INIT(a), NONZERO(a) ==> INIT(a)</li>
 * <li>Init one-of: INIT(a) <==> ONE_OF(ZERO(a), NONZERO(a))</li>
 * <li>MP: NONZERO(a), EQUIV(NONZERO(a), p) <==> NONZERO(a), p</li>
 * <li>Not MP: ZERO(a), EQUIV(NONZERO(a), p) <==> ZERO(a), OPPOS(p)</li>
 * <li>One of comm.: ONE_OF(p, q) <==> ONE_OF(q, p)</li>
 * <li>One of and false: ONE_OF(p, q), OPPOS(q) <==> p, OPPOS(q)</li>
 * <li>Logic AND: NONZERO(a), AND(a, b, c) <==> NONZERO(a), NONZERO(b), NONZERO(c)</li>
 * <li>Logic AND #2: ZERO(a), AND(a, b, c) <==> ZERO(a), ONE_OF(ZERO(b), ZERO(c))</li>
 * <li>Logic OR: ZERO(a), OR(a, b, c) <==> ZERO(a), ZERO(b), ZERO(c)</li>
 * <li>Logic OR #2: NONZERO(a), OR(a, b, c) <==> NONZERO(a), ONE_OF(NONZERO(b), NONZERO(c))</li>
 * <li>Constant subst.: EQUALS(a, const), P(a, b, ...) <==> EQUALS(a, const), P(const, b, ...)</li
 * <li>Value subst.: EQUALS(a, b), P(a, ...) ==> P(b, ...)</li>
 * <li>Opposite compares 1: OPPOS(LESS(a, b)) <==> LESS_EQ(b, a)</li>
 * <li>Opposite compares 2: OPPOS(LESS_EQ(a, b)) <==> LESS(b, a)</li>
 * <li>Compare conseq.: LESS(a, b) <==> LESS_EQ(a, b), NOT_EQ(a, b)</li>
 * <li>Compare union: ONE_OF(LESS(a, b), EQUALS(a, b)) <==> LESS_EQ(a, b)</li>
 * <li>Compare union #2: ONE_OF(LESS(a, b), LESS(b, a)) <==> NOT_EQ(a, b)</li>
 * <li>Equality refl.: EQUALS(a, b) <==> EQUALS(b, a)</li>
 * <li>Equality trans.: EQUALS(a, b), EQUALS(b, c) ==> EQUALS(a, c)</li>
 * <li>Equality trans. #2: EQUALS(a, b), EQUALS(b, const) <==> EQUALS(a, const), EQUALS(b, const)</li>
 * <li>Neg refl.: NEG(a, b) <==> NEG(b, a)</li>
 * <li>Neg anti-trans.: NEG(a, b), NEG(b, c) <==> EQUALS(a, c), NEG(b, c)</li>
 * <li>Neg & constant: NEG(a, const) <==> EQUALS(a, -const)</li>
 * <li>Sum comm.: SUM(a, b, c) <==> SUM(a, c, b)</li>
 * <li>Sum with zero: SUM(a, b, c), ZERO(c) <==> EQUALS(a, b), ZERO(c)
 * <li>Sum with zero #2: SUM(a, b, c), ZERO(a) <==> NEG(b, c), ZERO(a)
 * <li>Sum & neg: SUM(a, b, c), NEG(b, c) ==> ZERO(a)</li>
 * <li>Sum subst.: SUM(a, b, const1), SUM(b, c, const2) ==> SUM(a, b, const1+const2)</li>
 * <li>Prod comm.: PROD(a, b, c) <==> PROD(a, c, b)</li>
 * <li>Prod with zero: ZERO(c), PROD(a, b, c) <==> ZERO(a), ZERO(c)</li>
 * <li>Prod with zero #2: ZERO(a), PROD(a, b, c) <==> ZERO(a), ONE_OF(ZERO(b), ZERO(c))</li>
 * <li>Prod subst.: PROD(a, b, const1), PROD(b, c, const2) ==> PROD(a, c, const1*const2)</li>
 * <li>Points-to simple: POINTS_TO(x, a, b) ==> NONZERO(a)</li>
 * <li>Points-to + indirect: POINTS_TO(x, a, b), INDIRECT_EQ(a, c) <==> POINTS_TO(x, a, b), EQUALS(c, *(x+b))</li>
 * <li>Pointers difference: 
 * POINTS_TO(x, a1, c1), POINTS_TO(x, a2, c2), SUM(a1, a2, b) <==>
 * POINTS_TO(x, a1, c1), POINTS_TO(x, a2, c2), SUM(c1, c2, b)
 * </ul>
 * @author Mikhail Glukhikh
 */
public interface InferenceRule {

    /**
     * Точные правила не приводят к потери информации и применяются в
     * процессе упрощения. Неточные правила приводят к потере информации и
     * применяются при попытках что-то доказать.
     * @return true, если правило точное
     */
    public boolean isPrecise();

    /**
     * В правилах эквивалентности левая часть равнозначна правой.
     * Эквивалентные правила автоматически точные.
     * Преобразование Л ==> П применяется при упрощении.
     * Преобразование П ==> Л может применяться при попытках доказательства.
     * То есть, при прочих равных правая часть предпочтительнее левой.
     * @return true, если это правило эквивалентности
     */
    public boolean isEquivalent();

    /**
     * Получить левую часть правила
     * @return множество предикатов в левой части
     */
    public List<Predicate> getLeftPart();

    /**
     * Получить правую часть правила
     * @return множество предикатов в правой части
     */
    public List<Predicate> getRightPart();
}
