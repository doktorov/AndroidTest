package ru.test.doktorov.androidtest.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.test.doktorov.androidtest.app.GithubApi;
import ru.test.doktorov.androidtest.mvp.GithubService;

@Module(includes = {ApiModule.class})
public class GithubModule {
    @Provides
    @Singleton
    public GithubService provideGithubService(GithubApi authApi) {
        return new GithubService(authApi);
    }
}