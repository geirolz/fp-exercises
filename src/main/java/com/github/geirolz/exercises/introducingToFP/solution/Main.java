package com.github.geirolz.exercises.introducingToFP.solution;

import com.github.geirolz.exercises.introducingToFP.solution.model.Person;
import com.github.geirolz.exercises.introducingToFP.solution.abstractions.Option;
import com.github.geirolz.exercises.introducingToFP.solution.abstractions.Try;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@SuppressWarnings("Duplicates")
public class Main {

    @Test
    public void immutability() {

        Person person = new Person("Alan", 33, Option.none());

        Person personUpdated = new Person(
                person.getName(),
                person.getAge() + 1,
                person.getMoneyAmount());

        assert personUpdated.getAge() == 34;
    }

    @Test
    public void functions() {

        Person person = new Person("Martin", 27, Option.none());
        Function<Person, Integer> getAge = Person::getAge;
        Function<Integer, String> ageToString = Object::toString;
        Function<String, String> ageToPrettyString = age -> "Person age is: " + age;
        Function<String, String> strToUpperCase = String::toUpperCase;

        Function<Person, String> ageValueFromPerson = getAge
                .andThen(ageToString)
                .andThen(ageToPrettyString)
                .andThen(strToUpperCase);

        String result = ageValueFromPerson.apply(person);

        assert result.equals("PERSON AGE IS: 27");
    }

    @Test
    public void streams() {

        final List<Person> personList = asList(
                new Person("Alex"       , 56, Option.none()),
                new Person("Thomas"     , 12, Option.none()),
                new Person("Greg"       , 4, Option.none()),
                new Person("Victoria"   , 18, Option.none()),
                new Person("Maria"      , 23, Option.none()),
                new Person("Martin"     , 17, Option.none())
        );

        Predicate<Person> isAdult = p -> p.getAge() >= 18;
        Function<Person, String> getName = Person::getName;

        List<String> result = personList.stream()
                .filter(isAdult)
                .map(getName)
                .collect(Collectors.toList());

        assert result.containsAll(asList("Alex", "Victoria", "Maria"));
    }

    @Test
    public void options_map_flatMap() {

        Option<Person> personWithMoney =
                Option.some(new Person("David", 22, Option.some(new BigDecimal(100))));
        Option<Person> personWithoutMoney =
                Option.some(new Person("Franco", 22, Option.none()));

        //############### MAP ###############
        Function<Person, Integer> getAge = Person::getAge;
        Option<Integer> personWithMoneyAge = personWithMoney.map(getAge);
        Option<Integer> personWithoutMoneyAge = personWithoutMoney.map(getAge);


        assert personWithMoneyAge.isDefined();
        assert personWithMoneyAge.get() == 22;

        assert personWithoutMoneyAge.isDefined();
        assert personWithoutMoneyAge.get() == 22;

        //############### FLAT MAP ###############
        Function<Person, Option<BigDecimal>> getMoney = Person::getMoneyAmount;
        Option<BigDecimal> personWithMoneyAmount = personWithMoney.flatMap(getMoney);
        Option<BigDecimal> personWithoutMoneyAmount = personWithoutMoney.flatMap(getMoney);

        assert personWithMoneyAmount.isDefined();
        assert personWithMoneyAmount.get().equals(new BigDecimal(100));

        assert !personWithoutMoneyAmount.isDefined();
    }

    @Test
    public void options_getOrElse() {

        Option<Person> personWithMoney =
                Option.some(new Person("David", 22, Option.some(new BigDecimal(100))));
        Option<Person> personWithoutMoney =
                Option.some(new Person("Franco", 22, Option.none()));

        //############### GET OR ELSE WITH VALUE ###############
        BigDecimal personWithMoneyAmountOrElseValue = personWithMoney
                .flatMap(Person::getMoneyAmount)
                .getOrElse(BigDecimal.ZERO);
        BigDecimal personWithoutMoneyAmountOrElseValue = personWithoutMoney
                .flatMap(Person::getMoneyAmount)
                .getOrElse(BigDecimal.ZERO);

        assert personWithMoneyAmountOrElseValue.equals(new BigDecimal(100));
        assert personWithoutMoneyAmountOrElseValue.equals(BigDecimal.ZERO);

        //############### GET OR ELSE WITH SUPPLIER ###############
        final AtomicInteger personWithMoneySupplierCallCounter = new AtomicInteger(0);
        final AtomicInteger personWithoutMoneySupplierCallCounter = new AtomicInteger(0);

        BigDecimal personWithMoneyAmountOrElseSupplier = personWithMoney
                .flatMap(Person::getMoneyAmount)
                .getOrElse(() -> {
                    personWithMoneySupplierCallCounter.incrementAndGet();
                    return BigDecimal.ZERO;
                });
        BigDecimal personWithoutMoneyAmountOrElseSupplier = personWithoutMoney
                .flatMap(Person::getMoneyAmount)
                .getOrElse(() -> {
                    personWithoutMoneySupplierCallCounter.incrementAndGet();
                    return BigDecimal.ZERO;
                });

        assert personWithMoneyAmountOrElseSupplier.equals(new BigDecimal(100));
        assert personWithMoneySupplierCallCounter.get() == 0;

        assert personWithoutMoneyAmountOrElseSupplier.equals(BigDecimal.ZERO);
        assert personWithoutMoneySupplierCallCounter.get() == 1;
    }

    @Test
    public void errorHandlers_map_flatMap() {

        //TODO: implement Try methods
        Person personWithMoney = new Person("David", 22, Option.some(new BigDecimal(100)));
        Person personWithoutMoney = new Person("Marco", 22, Option.none());
        Function<BigDecimal, BigDecimal> multiply2 = p -> p.multiply(new BigDecimal(2));

        //############### MAP ###############
        Try<BigDecimal> resultMapPersonWithMoney =
                Try.ofSupplier(() -> personWithMoney.getMoneyAmount().get())
                        .map(multiply2);
        Try<BigDecimal> resultMapPersonWithoutMoney =
                Try.ofSupplier(() -> personWithoutMoney.getMoneyAmount().get())
                        .map(multiply2);

        assert !resultMapPersonWithMoney.isFailure();
        assert resultMapPersonWithMoney.get().equals(new BigDecimal(200));

        assert resultMapPersonWithoutMoney.isFailure();

        //############### FLAT MAP ###############
        Function<BigDecimal, Try<BigDecimal>> divideBy0 =
                p -> Try.ofSupplier(() -> p.divide(BigDecimal.ZERO, 0));

        Try<BigDecimal> resultFlatMapPersonWithMoney =
                Try.ofSupplier(() -> personWithMoney.getMoneyAmount().get())
                        .flatMap(divideBy0);
        Try<BigDecimal> resultFlatMapPersonWithoutMoney =
                Try.ofSupplier(() -> personWithoutMoney.getMoneyAmount().get())
                        .map(multiply2);

        assert resultFlatMapPersonWithMoney.isFailure();
        assert resultFlatMapPersonWithoutMoney.isFailure();
    }
}
