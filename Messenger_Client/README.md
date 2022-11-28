# Messenger_Client 📳

<br />

## Rest API 서버 설정

<br />
<br />

### BASE_URL 설정
###### com.messenger.main.retrofit.RetrofitInstance

```kotlin
package com.messenger.main.retrofit

import android.annotation.SuppressLint
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        @SuppressLint("AuthLeak")
        
        // 서버 주소 설정!!!
        private const val BASE_URL = "http://<서버주소>:<포트>/"
        var retrofit: Retrofit =
            Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}

```
