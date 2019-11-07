package com.github.geirolz.exercises.introducingToFP.ex.abstractions;

import com.github.geirolz.exercises.utils.exceptions.NotImplementedException;

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


    class Some<T> implements Option<T>{

        private final T value;

        Some(T value) {
            this.value = value;
        }

        @Override
        public boolean isDefined() { return true; }


        //TODO
        @Override
        public T get() {
            throw new NotImplementedException();
        }

        //TODO
        @Override
        public T getOrElse(T orValue) {
            throw new NotImplementedException();
        }

        //TODO
        @Override
        public T getOrElse(Supplier<T> supplier) {
            throw new NotImplementedException();
        }


        //TODO
        @Override
        public <K> Option<K> map(Function<T, K> mapper) {
            throw new NotImplementedException();
        }

        //TODO
        @Override
        public <K> Option<K> flatMap(Function<T, Option<K>> mapper) {
            throw new NotImplementedException();
        }
    }

    class None<T> implements Option<T>{

        @Override
        public boolean isDefined() { return false; }

        //TODO
        @Override
        public T get() {
            throw new NotImplementedException();
        }

        //TODO
        @Override
        public T getOrElse(T orValue) {
            throw new NotImplementedException();
        }

        //TODO
        @Override
        public T getOrElse(Supplier<T> supplier) {
            throw new NotImplementedException();
        }


        //TODO
        @Override
        public <K> Option<K> map(Function<T, K> mapper) {
            throw new NotImplementedException();
        }

        //TODO
        @Override
        public <K> Option<K> flatMap(Function<T, Option<K>> mapper) {
            throw new NotImplementedException();
        }
    }
}
