package com.rx.redis;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RxJavaRedis {

    // 连接redis
    private Jedis connectJedis() {
        Jedis  jedis = new Jedis("192.168.1.100",6379);
        jedis.auth("testredis");
        return jedis;
    }


    private Observable stringObservable(Jedis jedis,String... strings) {

        return Observable.create((ObservableOnSubscribe<String>) observableEmitter -> {
            JedisPubSub sub =new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    observableEmitter.onNext(message);
                }

            };
            // 订阅channel
            jedis.subscribe(sub, strings);
        });
    }

    public static void main(String[] asg) {
        RxJavaRedis rxJavaRedis = new RxJavaRedis();
        Observable observable = rxJavaRedis.stringObservable(rxJavaRedis.connectJedis(), "");
        Disposable subscribe = observable.subscribe(res -> System.out.println());
    }

}
