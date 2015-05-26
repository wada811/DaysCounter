package at.wada811.dayscounter.observable;

public class ObservableProperty<T> extends Observable<T> implements Observer<T> {

    private T value;

    public ObservableProperty(){
    }

    public ObservableProperty(T value){
        this.value = value;
    }

    public void setValue(T value){
        this.value = value;
        setChanged();
        notifyObservers(value);
        clearChanged();
    }

    public T getValue(){
        return value;
    }

    @Override
    public void update(Observable observable, T data){

    }

    public <TValue, TProperty> ObservableProperty<TProperty> createReactiveObservableProperty(){
        Func<TValue, TProperty> func = new Func<TValue, TProperty>() {
            @Override
            public TProperty apply(TValue value){
                return (TProperty)value;
            }
        };
        return createReactiveObservableProperty(func);
    }
    public <TValue, TProperty> ObservableProperty<TProperty> createReactiveObservableProperty(final Func<TValue, TProperty> func){
        ObservableProperty<TProperty> observableProperty = new ObservableProperty<TProperty>(func.apply((TValue)value)) {
            @Override
            public void update(Observable observable, TProperty value){
                super.update(observable, value);
                TProperty newValue = func.apply((TValue)value);
                if(value != newValue){
                    this.setValue(newValue);
                }
            }
        };
        addObserver(observableProperty);
        return observableProperty;
    }

}
