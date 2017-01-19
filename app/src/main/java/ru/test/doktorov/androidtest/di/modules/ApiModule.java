package ru.test.doktorov.androidtest.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import ru.test.doktorov.androidtest.app.GithubApi;

@Module(includes = {RetrofitModule.class})
public class ApiModule {
    @Provides
    @Singleton
    public GithubApi provideAuthApi(Retrofit retrofit) {
        return retrofit.create(GithubApi.class);
    }
}
