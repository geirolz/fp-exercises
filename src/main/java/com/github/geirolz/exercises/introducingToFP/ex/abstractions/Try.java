package com.github.geirolz.exercises.introducingToFP.ex.abstractions;


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Try<T>{

    static <T> Try<T> failure(Throwable cause) {
        return new Failure<>(cause);
    }

    static <T> Try<T> success(T t) {
        return new Success<>(t);
    }

    static <T> Try<T> ofSupplier(Supplier<T> supplier) {
        try{
            return success(supplier.get()) ;
        }catch (Throwable ex){
            return failure(ex);
        }
    }


    boolean isFailure();

    boolean isSuccess();

    T get();

    Throwable getCause();

    <B> Try<B> map(Function<T, B> mapper);

    <B> Try<B> flatMap(Function<T, Try<B>> mapper);


    class Success<T> implements Try<T> {

        private final T value;

        Success(T value) {
            this.value = value;
        }

        @Override
        public boolean isFailure() { return false; }

        @Override
        public boolean isSuccess() { return true; }

        @Override
        public T get() {
            return value;
        }




        //TODO
        @Override
        public Throwable getCause() {
            throw new NotImplementedException();
        }

        //TODO
        @Override
        public <B> Try<B> map(Function<T, B> mapper) {
            throw new NotImplementedException();
        }

        //TODO
        @Override
        public <B> Try<B> flatMap(Function<T, Try<B>> mapper) {
            throw new NotImplementedException();
        }
    }

    class Failure<T> implements Try<T> {

        private final Throwable cause;

        Failure(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public boolean isFailure() { return true; }

        @Override
        public boolean isSuccess() { return false; }



        //TODO
        @Override
        public T get(){
            throw new NotImplementedException();
        }

        //TODO
        @Override
        public Throwable getCause() {
            throw new NotImplementedException();
        }

        //TODO
        @Override
        public <B> Try<B> map(Function<T, B> mapper) {
            throw new NotImplementedException();
        }

        //TODO
        @Override
        public <B> Try<B> flatMap(Function<T, Try<B>> mapper) {
            throw new NotImplementedException();
        }
    }
}