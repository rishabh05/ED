//===============================================================================
// © 2015 eWorkplace Apps.  ALL rights reserved.
// Main Author: Shrey Sharma
// Original DATE: 05/2/2015
//===============================================================================
package com.eworkplaceapps.platform.utils;

/**
 * Tuple class
 */
public class Tuple<J, K, L> {

    /**
     * @param typeA of Type J
     * @param typeB of Type K
     * @param typeC of Type L
     * @param <J>
     * @param <K>
     * @param <L>
     * @return tuple
     */
    public static <J, K, L> Tuple2<J, K, L> tuple2(J typeA, K typeB, L typeC) {
        return new Tuple.Tuple2<J, K, L>(typeA, typeB, typeC);
    }

    /**
     * @param <J> of Type J
     * @param <K> of Type K
     * @param <L> of Type L
     */
    public static class Tuple2<J, K, L> {
        protected J typeA;
        protected K typeB;
        protected L typeC;

        public Tuple2(J typeA, K typeB, L typeC) {
            this.typeA = typeA;
            this.typeB = typeB;
            this.typeC = typeC;
        }

        public J getT1() {
            return typeA;
        }

        public K getT2() {
            return typeB;
        }

        public L getT3() {
            return typeC;
        }
    }
}
