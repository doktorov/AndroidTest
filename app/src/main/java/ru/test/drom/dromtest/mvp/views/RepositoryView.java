package ru.test.drom.dromtest.mvp.views;

import com.arellomobile.mvp.MvpView;

import ru.test.drom.dromtest.mvp.models.Repositories;

public interface RepositoryView extends MvpView {
    void showRepository(Repositories.Item user);
}

