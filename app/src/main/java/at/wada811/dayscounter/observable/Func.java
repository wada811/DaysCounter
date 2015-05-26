package at.wada811.dayscounter.observable;

public interface Func<TValue, TResult> {

    TResult apply(TValue value);

}
