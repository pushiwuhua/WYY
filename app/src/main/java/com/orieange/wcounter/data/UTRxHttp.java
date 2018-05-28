package com.orieange.wcounter.data;

import com.orieange.wcounter.AiLog;

import java.io.IOException;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * 响应式编程的Http通信管理类
 * 1. 处理网络不通
 * 2. 处理网络超时
 *
 * @author wzz created at 2016/10/27 17:01
 */
public class UTRxHttp {
    public String URL_STOCK_SH = "http://hq.sinajs.cn/list=s_sh%s";//沪市
    public String URL_STOCK_SZ = "http://hq.sinajs.cn/list=s_sz%s";//深市

    OkHttpClient okHttpClient;
    private static volatile UTRxHttp defaultInstance;

    private UTRxHttp() {
        okHttpClient = new OkHttpClient();
    }

    public static UTRxHttp get() {
        if (defaultInstance == null) {
            synchronized (UTRxHttp.class) {
                if (defaultInstance == null) {
                    defaultInstance = new UTRxHttp();
                }
            }
        }
        return defaultInstance;
    }

    public PublishSubject<Stock> getStock(final String code) {
        final PublishSubject<Stock> subject = PublishSubject.create();
        getString(String.format(Locale.getDefault(), URL_STOCK_SH, code))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Stock stock = CountUtil.decodeStock(s, code);
                        if (stock != null) {
                            subject.onNext(stock);
                            subject.onComplete();
                        }
                    }
                });

        getString(String.format(Locale.getDefault(), URL_STOCK_SZ, code))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Stock stock = CountUtil.decodeStock(s, code);
                        if (stock != null) {
                            subject.onNext(stock);
                            subject.onComplete();
                        }
                    }
                });
        return subject;
    }

    public Observable<String> getString(final String url) {

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) throws Exception {
                // step 2： 创建一个请求，不指定请求方法时默认是GET。
                Request.Builder requestBuilder = new Request.Builder().url(url);
                //可以省略，默认是GET请求
                requestBuilder.method("GET", null);
                // step 3：创建 Call 对象
                Call call = okHttpClient.newCall(requestBuilder.build());

                //step 4: 开始异步请求
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onComplete();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // TODO: 17-1-4 请求成功
                        //获得返回体
                        ResponseBody body = response.body();
                        String content = body.string();
                        AiLog.i(AiLog.TAG_WZZ, "StockFragment onResponse:" + content);
                        emitter.onNext(content);
                        emitter.onComplete();
                    }
                });
            }
        });
        return observable;
    }
}
