package com.example.saedolistocks5.outilsdivers;

/**
 * Quartet permettant de renvoyer quatre valeurs de quatre types différents ou non
 * @param <A> Le premier type
 * @param <B> Le deuxième type
 * @param <C> Le troisième type
 * @param <D> Le quatrième type
 * @param <E> Le cinquième type
 */
public class Quintet<A, B, C, D, E> {
    /**
     * Le premier paramètre.
     */
    private final A first;
    /**
     * Le deuxième paramètre.
     */
    private final B second;
    /**
     * Le troisième paramètre.
     */
    private final C third;
    /**
     * Le quatrième paramètre.
     */
    private final D fourth;
    /**
     * Le cinquième paramètre.
     */
    private final E fifth;

    /**
     * Initialisation du constructeur avec en paramètre les 4 paramètres .
     *
     * @param first  le premier
     * @param second le deuxième
     * @param third  le troisième
     * @param fourth le quatrième
     */
    public Quintet(A first, B second, C third, D fourth, E fifth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
    }

    /**
     * Premier de type A.
     *
     * @return le premier
     */
    public A first() {
        return first;
    }

    /**
     * Deuxième de type B.
     *
     * @return le deuxième
     */
    public B second() {
        return second;
    }

    /**
     * troisième de type C.
     *
     * @return le troisième
     */
    public C third() {
        return third;
    }

    /**
     * Quatrième de type D.
     *
     * @return le quatrième
     */
    public D fourth() {
        return fourth;
    }

    public E fifth() {
        return fifth;
    }
}
