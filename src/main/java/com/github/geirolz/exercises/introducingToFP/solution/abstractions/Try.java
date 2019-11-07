package com.github.geirolz.exercises.introducingToFP.solution.abstractions;


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




        @Override
        public Throwable getCause() {
            throw new RuntimeException("Can not invoke getCause on Success");
        }

        @Override
        public <B> Try<B> map(Function<T, B> mapper) {
            return Try.ofSupplier(() -> mapper.apply(value));
        }

        @Override
        public <B> Try<B> flatMap(Function<T, Try<B>> mapper) {
            return Try.ofSupplier(() -> mapper.apply(value).get());
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


        @Override
        public T get(){
            throw new RuntimeException(cause);
        }

        @Override
        public Throwable getCause() {
            return cause;
        }

        @Override
        public <B> Try<B> map(Function<T, B> mapper) {
            return Try.failure(cause);
        }

        //TODO
        @Override
        public <B> Try<B> flatMap(Function<T, Try<B>> mapper) {
            return Try.failure(cause);
        }
    }
}