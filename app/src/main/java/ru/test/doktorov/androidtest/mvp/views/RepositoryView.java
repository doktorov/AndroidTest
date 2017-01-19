package ru.test.doktorov.androidtest.mvp.views;

import com.arellomobile.mvp.MvpView;

import ru.test.doktorov.androidtest.mvp.models.Repositories;

public interface RepositoryView extends MvpView {
    void showRepository(Repositories.Item user);
}

