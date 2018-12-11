package com.example.overlord.eventapp;

import io.reactivex.Completable;
import io.reactivex.Observable;

import java.util.Optional;

public class ReactiveJavaTutorial {
    /**
     *  Android Samples
     *  https://github.com/amitshekhariitbhu/RxJava2-Android-Samples.git
     *
     *  Reasons :
     *
     *      > Video 1 from Jake Wharton, MindOrks
     *
     *      Async in imperative programming is very complicated
     *      Async will be memory leak if Activity out of focus
     *
     *      Reactive paradigm can decrease complexity in
     *      nested listeners or any combination of asynchronous tasks
     *
     *      Sources like (click events, database, network) are async
     *      And we set up listeners on them.
     *
     *      Then if these sources interact, we have to manage their communication
     *      ourselves by writing some complex code
     *
     *      Reactive is a framework (for multiple languages) that helps us simplify
     *      this entire process
     *
     *      This is a combination of multiple patterns (Observable, Functional, etc) .
     *
     *      There are two interfaces which make your sources Reactive
     *          Observable
     *          Flowable  (supports backpressure) (Reactive Streams Specification)
     *      Both types have very similar interface but implementation wise they are different
     *      Flowable supports backpressure which is like Flow Control from TCP
     *      It can request the source to change rate
     *
     *      Observable Paradigm
     *          Single          Reactive Scalar(Variable / Constant)
     *          Completable     Reactive Runnable
     *          Maybe           Reactive Optional ( Optional is a type which may hold a value or null,
     *                                              and you get a method to check its presence)
     */

    public interface User {
        String getUser();
        void setName(String name);
        void setAge(int age);
    }
    public interface ReactiveUser {
        Observable<String> getUser();
        Completable setName(String name);
        Completable setAge(String name);
    }

    public void test() {
        Optional<String> hello;
//        hello.isPresent();
    }
}
