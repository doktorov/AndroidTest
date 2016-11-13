package ru.test.drom.dromtest.mvp.common;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;
import ru.test.drom.dromtest.app.GithubError;

public class RxUtils {
    public static <T> Observable<T> wrapRetrofitCall(final Call<T> call) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> subscriber) throws Exception {
                final Response<T> execute;
                try {
                    execute = call.execute();
                } catch (IOException e) {
                    subscriber.onError(e);
                    return;
                }

                if (execute.isSuccessful()) {
                    subscriber.onNext(execute.body());
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    try {
                        stringBuilder.append(execute.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    subscriber.onError(new GithubError(stringBuilder.toString()));
                }
            }
        });
    }

    public static <T> Observable<T> wrapAsync(Observable<T> observable) {
        return wrapAsync(observable, Schedulers.io());
    }

    public static <T> Observable<T> wrapAsync(Observable<T> observable, Scheduler scheduler) {
        return observable
                .subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
