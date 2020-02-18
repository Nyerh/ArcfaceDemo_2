package com.arcsoft.arcfacedemo.common;

import android.app.Application;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.arcsoft.arcfacedemo.R;
import com.arcsoft.arcfacedemo.activity.ChooseFunctionActivity;
import com.arcsoft.arcfacedemo.greendao.DaoManager;
import com.arcsoft.arcfacedemo.model.User;
import com.arcsoft.arcfacedemo.util.PreferencesUtils;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
/**
 * APP共享
 */

public class App extends Application {
    private static App instance;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    private void init() {
        DaoManager.getInstance().init(this);
        activeEngine();
    }

    public static App getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void activeEngine() {
        boolean active = PreferencesUtils.getBoolean(this, Constants.ACTIVE, false);
        if (!active) {
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    FaceEngine faceEngine = new FaceEngine();
                    int activeCode = faceEngine.active(App.this, Constants.APP_ID, Constants.SDK_KEY);
                    emitter.onNext(activeCode);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer activeCode) {
                            if (activeCode == ErrorInfo.MOK) {
                                PreferencesUtils.putBoolean(App.this, Constants.ACTIVE, true);
                            } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
//                            showToast(getString(R.string.already_activated));
                            } else {
//                            showToast(getString(R.string.active_failed, activeCode));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }
    }

}
