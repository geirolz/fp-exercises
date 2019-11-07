package com.github.geirolz.exercises.introducingToFP.solution.abstractions;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Option<T> {

    static <T> Option<T> of(T value){
        return value == null ? none() : some(value);
    }

    static <T> Option<T> some(T value){
        return new Some<>(value);
    }

    static <T> Option<T> none(){
        return new None<>();
    }

    boolean isDefined();

    default boolean isEmpty(){ return !isDefined(); }

    T get();

    T getOrElse(T orValue);

    T getOrElse(Supplier<T> supplier);


    <K> Option<K> map(Function<T, K> mapper);

    <K> Option<K> flatMap(Function<T, Option<K>> mapper);


    class Some<T> implements Option<T> {

        private final T value;

        public Some(T value) {
            this.value = value;
        }

        @Override
        public boolean isDefined() { return true; }


        @Override
        public T get() {
            return value;
        }

        @Override
        public T getOrElse(T orValue) {
            return value;
        }

        @Override
        public T getOrElse(Supplier<T> supplier) {
            return value;
        }


        @Override
        public <K> Option<K> map(Function<T, K> mapper) {
            return Option.of(mapper.apply(value));
        }

        @Override
        public <K> Option<K> flatMap(Function<T, Option<K>> mapper) {
            return mapper.apply(value);
        }
    }

    class None<T> implements Option<T> {

        @Override
        public boolean isDefined() { return false; }

        @Override
        public T get() {
            throw new RuntimeException("Empty option");
        }

        @Override
        public T getOrElse(T orValue) {
            return orValue;
        }

        @Override
        public T getOrElse(Supplier<T> supplier) {
            return supplier.get();
        }


        @Override
        public <K> Option<K> map(Function<T, K> mapper) {
            return Option.none();
        }

        @Override
        public <K> Option<K> flatMap(Function<T, Option<K>> mapper) {
            return Option.none();
        }
    }
}
